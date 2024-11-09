package com.kesicollection.feature.habitpicker

import com.kesicollection.core.model.Habit

data class HabitPickerUiState(
    val displayedHabits: List<Habit> = emptyList(),
    val cacheHabits: List<Habit> = emptyList(),
    val selectedHabitId: String? = null,
    val isEditing: Boolean = false,
)

val initialState = HabitPickerUiState()