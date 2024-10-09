package com.kesicollection.core.redux.aliases

internal typealias Reducer<State> = TypedReducer<State,in Any>
internal typealias TypedReducer<State, Action> = (state: State, action: Action) -> State

internal typealias GetState<State> = () -> State
internal typealias Dispatcher = TypedDispatcher<Any>
internal typealias TypedDispatcher<Action> = (Action) -> Any

