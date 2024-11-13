package com.kesicollection.feature.addentry.model

import java.time.OffsetDateTime
import java.util.Locale

data class CreateDraftThunk(val offsetDateTime: OffsetDateTime, val locale: Locale)
data class UpdateTimeThunk(
    val hour: Int,
    val minute: Int
)

