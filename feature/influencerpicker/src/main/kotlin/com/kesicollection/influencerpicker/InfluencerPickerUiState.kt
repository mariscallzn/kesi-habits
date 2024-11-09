package com.kesicollection.influencerpicker

import com.kesicollection.core.model.Influencer

data class InfluencerPickerUiState(
    val displayedInfluencers: List<Influencer> = emptyList(),
    val selectedItems: Set<String> = emptySet(),
    val cacheInfluencers: List<Influencer> = emptyList(),
    val isEditing: Boolean = false,
)

val initialState = InfluencerPickerUiState()