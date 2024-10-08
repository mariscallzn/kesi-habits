package com.kesicollection.core.model

data class Week(
    // Days must be indexed in order and not exceed the real number of days in a week
    // this is to facilities view compositions
    val days: List<Day>
)