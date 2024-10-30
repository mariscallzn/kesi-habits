package com.kesicollection.feature.createemotion

data class CreateEmotionUiState(
    val createdEmotionId: String,
    val isCreateEnabled: Boolean = false
)

val initialState = CreateEmotionUiState("")