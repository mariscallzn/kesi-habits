package com.kesicollection.domain.datetime

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

class GetDateFromMillis @Inject constructor() {
    operator fun invoke(millis: Long?, locale: Locale, zoneId: ZoneId = ZoneOffset.UTC): String {
        return millis?.let {
            val instant = Instant.ofEpochMilli(millis)
            val localDate = instant.atZone(zoneId).toLocalDate()
            val currentDate = LocalDate.now()

            val formatter: DateTimeFormatter = if (localDate.year == currentDate.year) {
                DateTimeFormatter.ofPattern("MMM d", locale)
            } else {
                DateTimeFormatter.ofPattern("MMM d, yyyy", locale)
            }

            return localDate.format(formatter)
        } ?: ""
    }
}