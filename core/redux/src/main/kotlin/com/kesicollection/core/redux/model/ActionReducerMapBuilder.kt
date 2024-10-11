package com.kesicollection.core.redux.model

import com.kesicollection.core.redux.aliases.CaseReducer

interface ActionReducerMapBuilder<State> {
    fun <Returned, ThunkArg> addCase(
        actionCreator: ActionCreator<Returned, ThunkArg>,
        reducer: CaseReducer<State, Returned, ThunkArg>,
    ): ActionReducerMapBuilder<State>
}