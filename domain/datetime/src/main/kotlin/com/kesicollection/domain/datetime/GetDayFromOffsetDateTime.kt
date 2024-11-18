package com.kesicollection.domain.datetime

import com.kesicollection.core.model.Day
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.TextStyle
import java.util.Locale
import javax.inject.Inject

/**
 * Extracts [Day] information from a UTC `OffsetDateTime` object.

 * This class extracts the day of the week, day of the month, and the raw UTC `OffsetDateTime`
 * in ISO 8601 format from the given `OffsetDateTime`.

 * **Note:** The input `OffsetDateTime` must be in UTC format to ensure accurate day extraction.

 * @param zoneId The target time zone to convert the `OffsetDateTime` to.
 * @param getIsoFormatFromOffsetDateTime A delegate for formatting `OffsetDateTime` to ISO 8601 format.
 */
class GetDayFromOffsetDateTime @Inject constructor(
    private val zoneId: ZoneId,
    private val getIsoFormatFromOffsetDateTime: GetIsoFormatFromOffsetDateTime,
) {

    /**
     * Extracts [Day] information from a UTC `OffsetDateTime` object.

     * @param offsetDateTime The UTC `OffsetDateTime` object to extract day information from.
     * @param locale The locale to use for formatting the day of the week.
     * @return A [Day] object containing the extracted day information:
     *   - `dayOfWeek`: The day of the week formatted according to the specified locale.
     *   - `dayOfMonth`: The day of the month as a string.
     *   - `rawUTCDateTime`: The raw UTC `OffsetDateTime` in ISO 8601 format.
     */
    operator fun invoke(offsetDateTime: OffsetDateTime, locale: Locale): Day {
        val zonedDateTime = offsetDateTime.atZoneSameInstant(zoneId)
        val dayOfWeek = zonedDateTime.dayOfWeek.getDisplayName(TextStyle.SHORT, locale)
        val dayOfMonth = zonedDateTime.dayOfMonth

        return Day(
            dayOfWeek = dayOfWeek, dayOfMonth = dayOfMonth.toString(),
            rawUTCDateTime = getIsoFormatFromOffsetDateTime(offsetDateTime)
        )
    }
}