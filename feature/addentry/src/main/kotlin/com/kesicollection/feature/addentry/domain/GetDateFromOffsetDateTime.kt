package com.kesicollection.feature.addentry.domain

import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

class GetDateFromOffsetDateTime @Inject constructor(
    private val zoneId: ZoneId
) {
    operator fun invoke(offsetDateTime: OffsetDateTime, locale: Locale): String {
        val localDate = offsetDateTime.toInstant().atZone(zoneId).toLocalDate()
        val currentDate = LocalDate.now(zoneId)

        val formatter: DateTimeFormatter = if (localDate.year == currentDate.year) {
            DateTimeFormatter.ofPattern("MMM d", locale)
        } else {
            DateTimeFormatter.ofPattern("MMM d, yyyy", locale)
        }

        return localDate.format(formatter)
    }
}