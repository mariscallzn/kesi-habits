package com.kesicollection.core.model

data class HumanNeed(
    val id: Int,
    val name: String,
    val i18Key: String,
    val ranked: Int = -1,
)
