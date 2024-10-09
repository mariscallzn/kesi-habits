package com.kesicollection.core.model

import java.time.OffsetDateTime

data class Week(
    // Days must be indexed in order and not exceed the real number of days in a week
    // this is to facilities view compositions
    val days: List<OffsetDateTime>
)