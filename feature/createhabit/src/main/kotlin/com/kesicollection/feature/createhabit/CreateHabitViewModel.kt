package com.kesicollection.feature.createhabit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kesicollection.core.model.Classification
import com.kesicollection.core.model.Habit
import com.kesicollection.core.model.Status
import com.kesicollection.core.redux.creator.createAsyncThunk
import com.kesicollection.core.redux.creator.createStore
import com.kesicollection.core.redux.creator.reducer
import com.kesicollection.data.habit.HabitRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import java.util.UUID
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
    val createHabit = createAsyncThunk<String, Unit>("create-habit") { _, _ ->
        val uuid = "${UUID.randomUUID()}"
        habitRepository.add(
            Habit(
                uuid,
                habitName,
                classification ?: Classification.NEUTRAL,
                Status.ACTIVE
            )
        )
        uuid
    }.apply {
        store.builder.addCase(pending) { state, _ -> state.copy(isCreateButtonEnabled = false) }
        store.builder.addCase(fulfilled) { state, action ->
            action.payload.getOrNull()?.let { state.copy(createdHabitId = it) } ?: state
        }
        store.builder.addCase(rejected) { state, _ -> state }
    }
    //endregion
}