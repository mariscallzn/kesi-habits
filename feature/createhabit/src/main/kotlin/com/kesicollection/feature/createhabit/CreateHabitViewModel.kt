package com.kesicollection.feature.createhabit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kesicollection.core.model.Classification
import com.kesicollection.core.redux.creator.createStore
import com.kesicollection.core.redux.creator.reducer
import com.kesicollection.data.habit.HabitRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

//region Screen Actions
internal sealed class ScreenActions {
    data object Reset : ScreenActions()
    data object ValidateFields : ScreenActions()
    data object ClearText : ScreenActions()
}
//endregion

@HiltViewModel
class CreateHabitViewModel @Inject constructor(
    private val habitRepository: HabitRepository
) : ViewModel() {

    //region TextField state holders
    var habitName by mutableStateOf("")
        private set

    var classification by mutableStateOf<Classification?>(null)
        private set

    fun updateHabitName(value: String) {
        habitName = value
        dispatch(ScreenActions.ValidateFields)
    }

    fun updateClassification(value: Classification?) {
        classification = value
        dispatch(ScreenActions.ValidateFields)
    }

    //endregion

    private val store = createStore(
        coroutineScope = viewModelScope,
        initialState = initialState,
        reducer = reducer<CreateHabitUiState, ScreenActions> { state, action ->
            when (action) {
                is ScreenActions.Reset -> initialState

                is ScreenActions.ValidateFields ->
                    state.copy(isCreateButtonEnabled = habitName.isNotBlank() && classification != null)

                is ScreenActions.ClearText -> {
                    habitName = ""
                    classification = null
                    state.copy(isCreateButtonEnabled = false)
                }

            }
        }
    )

    val uiState = store.subscribe.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = initialState
    )

    fun dispatch(action: Any) {
        store.dispatch(action)
    }

    //region Async Thunks
    //endregion
}