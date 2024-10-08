package com.kesicollection.feature.weeklyhabits

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class WeeklyHabitsViewModel @Inject constructor() : ViewModel() {

    //TODO: Refactor (just for testing)
    val uiState = flow<WeeklyHabitsUiState> {
        emit(WeeklyHabitsUiState.Loading)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = WeeklyHabitsUiState.Loading
    )
}