package com.kesicollection.feature.createhabit

data class CreateHabitUiState(
    val createdHabitId: String,
    val isCreateButtonEnabled: Boolean
)

val initialState = CreateHabitUiState("", false)