package com.kesicollection.feature.weeklyhabits

sealed interface WeeklyHabitsUiState {
    data object Loading: WeeklyHabitsUiState
}