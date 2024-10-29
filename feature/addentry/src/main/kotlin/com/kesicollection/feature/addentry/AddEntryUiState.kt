package com.kesicollection.feature.addentry

import com.kesicollection.core.model.Habit
import com.kesicollection.feature.addentry.navigation.EntryDraftId

data class AddEntryUiState(
    val draftId: EntryDraftId,
    val coreHabit: Habit? = null,
    val triggerHabit: Habit? = null,
    val isSaveEnabled: Boolean,
)

val initialState = AddEntryUiState(
    draftId = "",
    isSaveEnabled = false
)