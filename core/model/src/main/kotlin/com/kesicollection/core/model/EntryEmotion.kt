package com.kesicollection.core.model

data class EntryEmotion(
    val id: Long = 0,
    val entryId: String,
    val emotionId: String,
    val emotionType: EmotionType
)
