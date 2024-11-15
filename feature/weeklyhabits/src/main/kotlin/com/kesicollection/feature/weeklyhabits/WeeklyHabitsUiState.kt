package com.kesicollection.feature.weeklyhabits

import com.kesicollection.core.model.Day

data class WeeklyHabitsUiState(
    val offsetIndex: Int,
    val weeks: List<List<Day>>,
    val selectedDay: Day = Day("",""),
    val currentDay: Day = Day("",""),

)

val initialState = WeeklyHabitsUiState(
    offsetIndex = 0,
    weeks = emptyList()
)