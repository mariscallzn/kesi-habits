package com.kesicollection.feature.addentry.domain

import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

class GetTimeFromOffsetDateTime @Inject constructor() {

    operator fun invoke(offsetDateTime: OffsetDateTime, locale: Locale): String {
        val formatter = DateTimeFormatter.ofPattern("h:mm a", locale)
        return offsetDateTime.format(formatter)
    }
}