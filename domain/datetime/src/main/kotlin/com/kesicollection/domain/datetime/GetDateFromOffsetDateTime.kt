package com.kesicollection.domain.datetime

import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

/**
 * A class that converts an `OffsetDateTime` to a formatted string representation based on the provided locale.
 *
 * This class uses dependency injection to obtain the target time zone (`zoneId`).
 *
 * @param zoneId The target time zone to convert the `OffsetDateTime` to.
 */
class GetDateFromOffsetDateTime @Inject constructor(
    private val zoneId: ZoneId
) {
    /**
     * Converts the provided `offsetDateTime` to a formatted string representation based on the locale.
     *
     * The format will be either "MMM d" for dates within the current year, or "MMM d, yyyy" for
     * dates in previous or future years.
     *
     * @param offsetDateTime The `OffsetDateTime` to be converted.
     * @param locale The locale to use for formatting the date.
     * @return The formatted string representation of the `offsetDateTime`.
     */
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