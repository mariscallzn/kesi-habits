package com.kesicollection.core.model

data class Influencer(
    val id: String,
    val name: String,
    val status: Status,
    val i18Key: String? = null
)
