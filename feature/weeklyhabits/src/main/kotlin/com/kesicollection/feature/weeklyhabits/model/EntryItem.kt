package com.kesicollection.feature.weeklyhabits.model

import com.kesicollection.core.model.Classification

data class EntryItem(
    val title: String,
    val time: String,
    val classification: Classification,
    val humanNeeds: List<String>,
)