package com.kesicollection.feature.weeklyhabits

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kesicollection.core.model.Habit
import com.kesicollection.data.habit.HabitRepository
import com.kesicollection.data.weekspaging.WeekPagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class WeeklyHabitsViewModel @Inject constructor(
    private val habitRepository: HabitRepository,
    private val weekPagingSource: WeekPagingSource
) : ViewModel() {

    override fun onCleared() {
        super.onCleared()
        weekPagingSource.clear()
    }

    //TODO: Refactor (just for testing)
    val uiState = flow<WeeklyHabitsUiState> {
        emit(WeeklyHabitsUiState.Loading)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = WeeklyHabitsUiState.Loading
    )

    fun addHabit() {
        viewModelScope.launch {
            habitRepository.addOrUpdateHabits(
                listOf(
                    Habit(
                        "",
                        "This is a test ${Random.nextInt()}",
                        "TODO: re-evaluate data class"
                    )
                )
            )
        }
    }
}