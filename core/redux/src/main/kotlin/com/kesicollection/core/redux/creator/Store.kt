package com.kesicollection.core.redux.creator

import com.kesicollection.core.redux.aliases.Dispatcher
import com.kesicollection.core.redux.aliases.GetState
import com.kesicollection.core.redux.aliases.Reducer
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

    }
}

internal class StoreManagerImpl<State>(
    initialState: State,
    val reducer: Reducer<State>,
    private val coroutineScope: CoroutineScope,
) : StoreManager<State> {

    private val _listener = MutableStateFlow(initialState)
    private val listener: StateFlow<State>
        get() = _listener

    private var currentState: State = initialState

    override fun dispatch(action: Any): Any {
        currentState = reducer(currentState, action)
        coroutineScope.launch {
            _listener.emit(currentState)
        }
        return action
    }

    override fun subscribe(): Flow<State> = listener

    override fun getState(): State = currentState
}