package com.kesicollection.feature.emotionpicker

import com.kesicollection.core.model.Emotion

data class EmotionPickerUiState(
    val emotions: List<Emotion> = emptyList(),
    val cacheEmotions: List<Emotion> = emptyList(),
    val selectedItems: Set<String> = emptySet()
)

val initialState = EmotionPickerUiState()