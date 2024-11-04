package com.kesicollection.feature.emotionpicker

import com.kesicollection.core.model.Emotion

data class EmotionPickerUiState(
    val displayedEmotions: List<Emotion> = emptyList(),
    val selectedItems: Set<String> = emptySet(),
    val cacheEmotions: List<Emotion> = emptyList(),
    val isEditing: Boolean = false,
)

val initialState = EmotionPickerUiState()