package com.kesicollection.core.model

import java.time.OffsetDateTime

data class Entry(
    val id: String,
    val habit: Habit,
    val recordedOn: OffsetDateTime,
    val status: Status,
    val triggeredBy: Habit?,
    val influencers: List<Influencer>?,
    val currentEmotions: List<Emotion>?,
    val desiredEmotions: List<Emotion>?
)
