package com.kesicollection.feature.weeklyhabits

import androidx.lifecycle.viewModelScope
import com.kesicollection.core.redux.ReduxViewModel
import com.kesicollection.core.redux.creator.createStore
import com.kesicollection.core.redux.creator.reducer
import com.kesicollection.core.redux.model.Store
import com.kesicollection.data.habit.HabitRepository
import com.kesicollection.data.weekspaging.WeekPagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

sealed class ScreenAction {
    data object One : ScreenAction()
    data object Two : ScreenAction()
}

@HiltViewModel
class WeeklyHabitsViewModel @Inject constructor(
    private val habitRepository: HabitRepository,
    private val weekPagingSource: WeekPagingSource
) : ReduxViewModel<WeeklyHabitsUiState>() {

    val uiState = store.subscribe.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = store.state
    )

    override fun configureStore(): Store<WeeklyHabitsUiState> =
        createStore(
            coroutineScope = viewModelScope,
            initialState = initialState,
            reducer = reducer<WeeklyHabitsUiState, ScreenAction> { state, action ->
                when (action) {
                    ScreenAction.One -> state.copy(counter = state.counter + 1)
                    ScreenAction.Two -> state
                }
            },
        )


    override fun onCleared() {
        super.onCleared()
        weekPagingSource.clear()
    }

}