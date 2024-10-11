package com.kesicollection.core.redux.creator

import com.kesicollection.core.redux.aliases.CaseReducer
import com.kesicollection.core.redux.aliases.Dispatcher
import com.kesicollection.core.redux.aliases.GetState
import com.kesicollection.core.redux.aliases.Reducer
import com.kesicollection.core.redux.model.ActionCreator
import com.kesicollection.core.redux.model.ActionReducerMapBuilder
import com.kesicollection.core.redux.model.AsyncThunkAction
import com.kesicollection.core.redux.model.AsyncThunkOptions
import com.kesicollection.core.redux.model.PayloadAction
import com.kesicollection.core.redux.model.Store
import com.kesicollection.core.redux.model.StoreManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


fun <State> createStore(
    reducer: Reducer<State>,
    initialState: State,
    coroutineScope: CoroutineScope,
): Store<State> {
    val storeManager = StoreManagerImpl(initialState, reducer, coroutineScope)
    return object : Store<State> {
        override val getState: GetState<State>
            get() = storeManager::getState
        override val dispatch: Dispatcher
            get() = storeManager::dispatch
        override val subscribe: Flow<State>
            get() = storeManager.subscribe()
        override val builder: ActionReducerMapBuilder<State>
            get() = storeManager
    }
}

internal class StoreManagerImpl<State>(
    initialState: State,
    val reducer: Reducer<State>,
    private val coroutineScope: CoroutineScope,
) : StoreManager<State>, ActionReducerMapBuilder<State> {

    private val _stateFlow = MutableStateFlow(initialState)
    private val stateFlow: StateFlow<State>
        get() = _stateFlow

    private var currentState: State = initialState

    private val actionReducerMapBuilder = mutableMapOf<String, CaseReducer<State, *, *>>()

    override fun subscribe(): Flow<State> = stateFlow

    override fun getState(): State = currentState

    override fun dispatch(action: Any): Any {
        if (action is AsyncThunkAction<*, *>) {
            dispatchAsync(action)
        } else {
            currentState = reducer(currentState, action)
            coroutineScope.launch {
                _stateFlow.emit(currentState)
            }
        }
        return action
    }

    private fun <Returned, ThunkArg> dispatchAsync(thunkAction: AsyncThunkAction<Returned, ThunkArg>) {
        suspend fun updateStateAndEmit(actionType: String, result: Result<Returned>) {
            actionReducerMapBuilder[actionType]?.let { reducer ->
                currentState = reducer(
                    getState(),
                    PayloadAction(result, actionType, thunkAction.arg)
                )
                _stateFlow.emit(currentState)
            }
//           ?: run {
//                //TODO: Set a flag that during Dev this will happen but in prod the exception will be ignored
//                if (actionType == "rejected") throw IllegalArgumentException("")
//            }
        }

        coroutineScope.launch {
            updateStateAndEmit(
                thunkAction.asyncThunk.pending.type,
                Result.failure(IllegalAccessException("Payload cannot be accessed in a pending state"))
            )

            try {
                val result = thunkAction.reducerPayload(
                    thunkAction.arg,
                    AsyncThunkOptions(::dispatch, getState() as Any)
                )
                //TODO: Delete this, testing exceptions only
                if (thunkAction.arg == "Boom") throw IllegalStateException("Boom happened")
                updateStateAndEmit(thunkAction.asyncThunk.fulfilled.type, Result.success(result))
            } catch (e: Exception /* This will catch CancellationException which for now it's ok */) {
                updateStateAndEmit(thunkAction.asyncThunk.rejected.type, Result.failure(e))
            }
        }
    }

    override fun <Returned, ThunkArg> addCase(
        actionCreator: ActionCreator<Returned, ThunkArg>,
        reducer: CaseReducer<State, Returned, ThunkArg>
    ): ActionReducerMapBuilder<State> {
        /**
         * Since we don't have access to the specific `Returned` and `ThunkArg` types until the case is added,
         * we cast the [CaseReducer] reference to the generic types defined on the [actionReducerMapBuilder] global variable.
         * While we don't need the exact types for this purpose, this ensures we have a valid [CaseReducer] reference.
         * Each case will be handled individually within [dispatchAsync], where the action is of type [AsyncThunkAction]
         * with the correct `Returned` and `ThunkArg` types as defined at the time of dispatch.
         */
        @Suppress("UNCHECKED_CAST")
        actionReducerMapBuilder[actionCreator.type] = reducer as CaseReducer<State, *, *>
        return this
    }
}