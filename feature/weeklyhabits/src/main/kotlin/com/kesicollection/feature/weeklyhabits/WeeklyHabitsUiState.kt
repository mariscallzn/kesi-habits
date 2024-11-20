package com.kesicollection.feature.weeklyhabits

import com.kesicollection.core.model.Day
import com.kesicollection.core.model.Entry
import com.kesicollection.feature.weeklyhabits.model.EntryItem
import java.util.Locale

data class WeeklyHabitsUiState(
    val offsetIndex: Int = 0,
    val weeks: List<List<Day>> = emptyList(),
    val entries: Map<Day, List<EntryItem>> = emptyMap(),
    val selectedDay: Day = Day("", "", ""),
    val currentDay: Day = Day("", "", ""),
    val displayedDate: String = "",
    val currentLocale: Locale = Locale.getDefault(),
)

val initialState = WeeklyHabitsUiState()