package com.kesicollection.core.redux.model

import kotlinx.coroutines.flow.Flow

internal interface StoreManager<State> {
    fun dispatch(action: Any): Any
    fun subscribe(): Flow<State>
    fun getState(): State
}