package com.kesicollection.domain.datetime

import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class GetFormattedOffsetDateTime @Inject constructor() {
    private val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
    operator fun invoke(offsetDateTime: OffsetDateTime): String =
        offsetDateTime.format(formatter)
}