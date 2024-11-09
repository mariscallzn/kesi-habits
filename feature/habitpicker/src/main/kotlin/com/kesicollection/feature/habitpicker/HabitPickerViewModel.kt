package com.kesicollection.feature.habitpicker

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kesicollection.core.model.Habit
import com.kesicollection.core.redux.creator.createAsyncThunk
import com.kesicollection.core.redux.creator.createStore
import com.kesicollection.core.redux.creator.reducer
import com.kesicollection.data.habit.HabitRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

//region Screen Actions
internal sealed class ScreenActions {
    data object Reset : ScreenActions()
    data object ClearTextField : ScreenActions()
    data class Filter(val searchTerm: String) : ScreenActions()
    data class SelectHabit(val habitId: String) : ScreenActions()
}
//endregion

@HiltViewModel
class HabitPickerViewModel @Inject constructor(
    private val repo: HabitRepository
) : ViewModel() {

    //region TextField state holder
    var searchTerm by mutableStateOf("")
        private set

    fun updateSearchTerm(value: String) {
        searchTerm = value
        dispatch(ScreenActions.Filter(value))
    }
    //endregion

    private val store = createStore(
        coroutineScope = viewModelScope,
        initialState = initialState,
        reducer = reducer<HabitPickerUiState, ScreenActions> { state, action ->
            when (action) {
                is ScreenActions.Reset -> initialState
                is ScreenActions.ClearTextField -> {
                    searchTerm = ""
                    filter(state, searchTerm)
                }

                is ScreenActions.Filter -> filter(state, action.searchTerm)
                is ScreenActions.SelectHabit -> state.copy(selectedHabitId = action.habitId)
            }
        }
    )

    val uiState = store.subscribe.onStart { dispatch(loadHabits(Unit)) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = initialState
        )

    fun dispatch(action: Any) {
        store.dispatch(action)
    }

    private fun filter(state: HabitPickerUiState, term: String): HabitPickerUiState {
        return if (term.isNotBlank()) state.copy(displayedHabits = state.cacheHabits.filter {
            it.name.contains(
                term,
                ignoreCase = true
            )
        }) else state.copy(
            displayedHabits = state.cacheHabits
        )
    }

    //region Async Thunks
    val loadHabits = createAsyncThunk<List<Habit>, Unit>("load-habits") { _, _ ->
        repo.fetch()
    }.apply {
        store.builder.addCase(fulfilled) { s, a ->
            a.payload.getOrNull()?.let { s.copy(displayedHabits = it, cacheHabits = it) } ?: s
        }
    }
    //endregion
}