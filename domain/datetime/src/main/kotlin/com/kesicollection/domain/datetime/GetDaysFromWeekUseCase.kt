package com.kesicollection.domain.datetime

import com.kesicollection.core.model.Day
import com.kesicollection.core.model.Week
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

/**
 * A use case that extracts a list of [Day] objects for each day within a provided [Week].
 *
 * This use case iterates through the `days` list of the [Week] object and transforms each
 * `OffsetDateTime` to a [Day] object. Each [Day] object contains:
 *  - `dayOfWeek`: The formatted day of the week (e.g., "Mon", "Tue") based on the provided [Locale].
 *  - `dayOfMonth`: The day of the month as a two-digit string (e.g., "01", "22").
 *  - `rawUTCDateTime`: The raw UTC `OffsetDateTime` in ISO 8601 format.
 *
 * @param getIsoFormatFromOffsetDateTime A function that formats an `OffsetDateTime` to ISO 8601 format.
 * @param zoneId The [ZoneId] used for formatting the day of the week and day of the month.
 */
class GetDaysFromWeekUseCase @Inject constructor(
    private val getIsoFormatFromOffsetDateTime: GetIsoFormatFromOffsetDateTime,
    private val zoneId: ZoneId,
) {

    /**
     * Extracts a list of [Day] objects for each day within the provided [Week].
     *
     * @param week The [Week] object containing the list of `OffsetDateTime` objects representing the days of the week.
     * @param locale The [Locale] to use for formatting the day of the week and day of the month.
     * @return A list of [Day] objects, one for each day in the [Week].
     */
    operator fun invoke(week: Week, locale: Locale): List<Day> = week.days.map { offsetDateTime ->
        val formatterDayOfWeek = DateTimeFormatter.ofPattern("EEE", locale)
        val formatterDayOfMonth = DateTimeFormatter.ofPattern("dd", locale)
        Day(
            dayOfWeek = offsetDateTime.toInstant().atZone(zoneId).format(formatterDayOfWeek),
            dayOfMonth = offsetDateTime.toInstant().atZone(zoneId).format(formatterDayOfMonth),
            rawUTCDateTime = getIsoFormatFromOffsetDateTime(offsetDateTime)
        )
    }
}