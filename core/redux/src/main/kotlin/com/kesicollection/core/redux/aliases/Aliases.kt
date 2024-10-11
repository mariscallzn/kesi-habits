package com.kesicollection.core.redux.aliases

import com.kesicollection.core.redux.model.AsyncThunkOptions
import com.kesicollection.core.redux.model.PayloadAction

internal typealias Reducer<State> = TypedReducer<State,in Any>
internal typealias TypedReducer<State, Action> = (state: State, action: Action) -> State

internal typealias GetState<State> = () -> State
internal typealias Dispatcher = TypedDispatcher<Any>
internal typealias TypedDispatcher<Action> = (Action) -> Any

typealias ReducerPayload<Returned, ThunkArg> = suspend (arg: ThunkArg, options: AsyncThunkOptions) -> Returned
typealias CaseReducer<State, Returned, ThunkArg>
        = (state: State, action: PayloadAction<Returned, ThunkArg>) -> State

