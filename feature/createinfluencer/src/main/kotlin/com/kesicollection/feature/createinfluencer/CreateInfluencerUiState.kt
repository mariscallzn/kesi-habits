package com.kesicollection.feature.createinfluencer

data class CreateInfluencerUiState(
    val createdInfluencerId: String = "",
    val isCreateEnable: Boolean = false
)

val initialState = CreateInfluencerUiState()