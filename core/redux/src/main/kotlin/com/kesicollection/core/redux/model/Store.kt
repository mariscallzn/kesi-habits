package com.kesicollection.core.redux.model

import com.kesicollection.core.redux.aliases.Dispatcher
import com.kesicollection.core.redux.aliases.GetState
import kotlinx.coroutines.flow.Flow

interface Store<State> {
    val getState: GetState<State>
    val state: State get() = getState()
    val dispatch: Dispatcher
    val subscribe: Flow<State>
    val builder: ActionReducerMapBuilder<State>
}