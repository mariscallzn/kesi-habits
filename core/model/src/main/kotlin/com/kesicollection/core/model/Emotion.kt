package com.kesicollection.core.model

data class Emotion(
    val id: String,
    val name: String,
    val valence: Valence,
    val arousal: Arousal,
    val i18Key: String?
)