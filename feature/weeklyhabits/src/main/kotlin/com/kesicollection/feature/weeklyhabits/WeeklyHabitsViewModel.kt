package com.kesicollection.feature.weeklyhabits

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kesicollection.core.redux.creator.createAsyncThunk
import com.kesicollection.core.redux.creator.createStore
import com.kesicollection.core.redux.creator.reducer
import com.kesicollection.data.habit.HabitRepository
import com.kesicollection.data.weekspaging.WeekPagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import kotlin.random.Random

sealed class ScreenAction {
    data object One : ScreenAction()
    data object Two : ScreenAction()
}

@HiltViewModel
class WeeklyHabitsViewModel @Inject constructor(
    private val habitRepository: HabitRepository,
    private val weekPagingSource: WeekPagingSource
) : ViewModel() {

    private val store = createStore(
        coroutineScope = viewModelScope,
        initialState = initialState,
        reducer = reducer<WeeklyHabitsUiState, ScreenAction> { state, action ->
            when (action) {
                ScreenAction.One -> state.copy(status = "ScreenAction.One ${Random.nextInt()}")
                ScreenAction.Two -> state.copy(status = "Action dispatched within asyncThunk")
            }
        }
    )

    val uiState = store.subscribe.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = store.state
    )

    fun dispatch(action: Any) {
        store.dispatch(action)
    }

    val test = createAsyncThunk<String, String>("test") { args, options ->
        delay(1_500)
        options.dispatch(ScreenAction.Two)
        Log.d("Andres", "Let's log the state ${options.getState as WeeklyHabitsUiState} ")
        delay(1_500)
        "args: $args Ahuevo"
    }.apply {
        store.builder.addCase(pending) { s, a ->
            s.copy(status = "pending -> action $a")
        }
        store.builder.addCase(fulfilled) { s, a ->
            s.copy(status = "fulfilled -> action $a")
        }
        store.builder.addCase(rejected) { s, a ->
            s.copy(status = "rejected -> action $a")
        }
    }

    override fun onCleared() {
        super.onCleared()
        weekPagingSource.clear()
    }

}