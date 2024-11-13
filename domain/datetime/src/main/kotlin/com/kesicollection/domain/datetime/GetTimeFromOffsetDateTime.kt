package com.kesicollection.domain.datetime

import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

class GetTimeFromOffsetDateTime @Inject constructor(
    private val zoneId: ZoneId
) {

    operator fun invoke(offsetDateTime: OffsetDateTime, locale: Locale): String {
        val formatter = DateTimeFormatter.ofPattern("h:mm a", locale)
        val zonedDateTime = offsetDateTime.atZoneSameInstant(zoneId)
        return zonedDateTime.format(formatter)
    }
}