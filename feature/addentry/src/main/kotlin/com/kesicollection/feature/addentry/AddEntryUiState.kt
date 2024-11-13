package com.kesicollection.feature.addentry

import com.kesicollection.core.model.Emotion
import com.kesicollection.core.model.Habit
import com.kesicollection.core.model.Influencer
import com.kesicollection.feature.addentry.navigation.EntryDraftId
import java.util.Locale

data class AddEntryUiState(
    val draftId: EntryDraftId,
    val coreHabit: Habit? = null,
    val triggerHabit: Habit? = null,
    val currentEmotions: List<Emotion> = emptyList(),
    val desireEmotions: List<Emotion> = emptyList(),
    val influencers: List<Influencer> = emptyList(),
    val isSaveEnabled: Boolean = false,
    val isTimeShowing: Boolean = false,
    val isDateShowing: Boolean = false,
    val formattedDate: String = "",
    val formattedTime: String = "",
    val time: Pair<Int, Int> = 0 to 0,
    val locale: Locale = Locale.getDefault(),
    val recordedOn: String? = null,
)

val initialState = AddEntryUiState(
    draftId = ""
)