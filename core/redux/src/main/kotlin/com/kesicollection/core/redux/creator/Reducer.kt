package com.kesicollection.core.redux.creator

import com.kesicollection.core.redux.aliases.Reducer
import com.kesicollection.core.redux.aliases.TypedReducer

inline fun <State, reified Action> reducer(
    crossinline reducer: TypedReducer<State, Action>
): Reducer<State> = { state, action ->
    when(action) {
        is Action -> reducer(state, action)
        else -> state
    }
}

/**
 * Combines two reducers with the + operator.
 */
operator fun <State> Reducer<State>.plus(other: Reducer<State>): Reducer<State> = { s, a ->
    other(this(s, a), a)
}