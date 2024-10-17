package com.kesicollection.feature.addentry

data class AddEntryUiState(val saveEnabled: Boolean)

val initialState = AddEntryUiState(saveEnabled = false)