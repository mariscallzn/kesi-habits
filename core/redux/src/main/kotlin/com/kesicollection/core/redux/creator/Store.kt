package com.kesicollection.core.redux.creator

import com.kesicollection.core.redux.aliases.Dispatcher
import com.kesicollection.core.redux.aliases.GetState
import com.kesicollection.core.redux.aliases.Reducer
import com.kesicollection.core.redux.model.Store
import com.kesicollection.core.redux.model.StoreManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

//TODO Verify types
typealias CaseReducer<State, Any> = (state: State, action: Any) -> State

interface ActionReducerMapBuilder<State> {
    fun addCase(
        type: String,
        reducer: CaseReducer<State, Any>,
    ): ActionReducerMapBuilder<State>
}

fun <State> createStore(
    reducer: Reducer<State>,
    initialState: State,
    extraReducers: (builder: ActionReducerMapBuilder<State>) -> Unit,
    coroutineScope: CoroutineScope,
): Store<State> {
    val storeManager = StoreManagerImpl(initialState, reducer, coroutineScope)
    println("Andres $storeManager extraReducers $extraReducers")
    extraReducers(storeManager)
    return object : Store<State> {
        override val getState: GetState<State>
            get() = storeManager::getState
        override val dispatch: Dispatcher
            get() = storeManager::dispatch
        override val subscribe: Flow<State>
            get() = storeManager.subscribe()

    }
}

internal class StoreManagerImpl<State>(
    initialState: State,
    val reducer: Reducer<State>,
    private val coroutineScope: CoroutineScope,
) : StoreManager<State>, ActionReducerMapBuilder<State> {

    private val _listener = MutableStateFlow(initialState)
    private val listener: StateFlow<State>
        get() = _listener

    private var currentState: State = initialState

    private val actionReducerMapBuilder = mutableMapOf<String, CaseReducer<State, Any>>()

    override fun dispatch(action: Any): Any {
        println("Andres dispatch $action")
        if (action is AsyncThunkAction<*, *>) {
            println("Andres AsyncThunkAction")
            dispatchAsync(action)
        } else {
            currentState = reducer(currentState, action)
            coroutineScope.launch {
                _listener.emit(currentState)
            }
        }
        return action
    }

    private fun <Returned, ThunkArg> dispatchAsync(thunkAction: AsyncThunkAction<Returned, ThunkArg>) {
        println("Andres dispatchAsync $thunkAction")
        coroutineScope.launch {
            actionReducerMapBuilder[thunkAction.asyncThunk.pending]?.let {
                println("Andres dispatchAsync pending")
                currentState = it(getState(), Unit)
                println("Andres dispatchAsync emit updated pending state $currentState")
                _listener.emit(currentState)
            }
            val referredResult = async {
                thunkAction.reducerPayload(
                    thunkAction.arg,
                    AsyncThunkOptions(::dispatch, getState() as Any)
                )
            }

            try {
                delay(3_000)
                val result = referredResult.await()
                actionReducerMapBuilder[thunkAction.asyncThunk.fulfilled]?.let {
                    println("Andres dispatchAsync fulfilled")
                    currentState = it(getState(), result as Any)
                    println("Andres dispatchAsync emit updated fulfilled state $currentState")
                    _listener.emit(currentState)
                }
            } catch (e: Exception) {
                actionReducerMapBuilder[thunkAction.asyncThunk.rejected]?.let {
                    println("Andres dispatchAsync rejected")
                    currentState = it(getState(), Unit)
                    _listener.emit(currentState)
                }
            }
        }
    }

    override fun subscribe(): Flow<State> = listener

    override fun getState(): State = currentState

    override fun addCase(
        type: String,
        reducer: CaseReducer<State, Any>
    ): ActionReducerMapBuilder<State> {
        println("Andres case added $type reducer $reducer")
        actionReducerMapBuilder[type] = reducer
        return this
    }
}