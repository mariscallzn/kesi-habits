package com.kesicollection.core.model

import java.time.OffsetDateTime

data class Entry(
    val id: String,
    val recordedOn: OffsetDateTime,
    val status: Status,
    val habit: Habit? = null,
    val triggeredBy: Habit? = null,
    val influencers: List<Influencer>? = null,
    val humanNeeds: List<HumanNeed>? = null
)
