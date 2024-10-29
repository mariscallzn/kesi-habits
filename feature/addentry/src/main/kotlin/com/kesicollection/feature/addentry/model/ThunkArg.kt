package com.kesicollection.feature.addentry.model

import com.kesicollection.core.model.HabitType
import com.kesicollection.feature.addentry.navigation.EntryDraftId

data class UpdateHabitThunkArgs(
    val entryDraftId: EntryDraftId,
    val habitId: String?, val habitType: HabitType)