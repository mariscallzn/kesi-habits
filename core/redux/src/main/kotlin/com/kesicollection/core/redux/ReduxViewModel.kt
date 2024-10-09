package com.kesicollection.core.redux

import androidx.lifecycle.ViewModel
import com.kesicollection.core.redux.model.Store

/**
 * References:
 * - https://reduxkotlin.org/introduction/getting-started
 * - https://redux.js.org/introduction/getting-started
 *
 * Stores are normally created at the application level on a React/N apps
 *
 * But here I'm planning to create a local "Store" per ViewModel since a view model can be shared
 * across different views/fragments/composable components.
 *
 * The main objective for this tho is to tackle composable screens.
 *
 * A composable screen must have an UIState compose but this could be composed by mini UIPortionState
 * for example:
 * - A Master-Details screen where list are on one side and detail on the other.
 * - Horizontal List combined with a vertical List.
 *
 * For that reason a ViewModel will host an instance of a "Store"
 *
 * ReduxViewModel needs a Store where the state and all the reducers will be listed as well as
 * all the thunks that needs to be registered.
 */
abstract class ReduxViewModel<State> : ViewModel() {
    val store = this.configureStore()

    abstract fun configureStore(): Store<State>

    fun dispatch(action: Any) {
        store.dispatch(action)
    }
}