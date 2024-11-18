package com.kesicollection.domain.datetime

import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.Locale
import javax.inject.Inject

/**
 * A class that converts an `OffsetDateTime` to a textual representation based on the provided
 * locale, considering the relative date to today.

 * **Note:** Full internationalization (i18n) is not yet implemented. Currently, relative terms
 * ("Today", "Yesterday", "Tomorrow") are not localized.

 * This class uses dependency injection to obtain the target time zone (`zoneId`) and a delegate for
 * basic date formatting (`getDateFromOffsetDateTime`).

 * @param zoneId The target time zone to convert the `OffsetDateTime` to.
 * @param getDateFromOffsetDateTime A delegate for formatting `OffsetDateTime` to a basic string
 * representation.
 *
 * @todo Full i18n needs to be implemented to return the right language based on the provided locale.
 */
class GetDisplayedDateFromOffsetDateTime @Inject constructor(
    private val zoneId: ZoneId,
    private val getDateFromOffsetDateTime: GetDateFromOffsetDateTime
) {
    /**
     * Converts the provided `offsetDateTime` to a textual representation based on the locale, considering the relative date to today.

     * The function will return "Today" if the date is the same as today, "Yesterday" if it's one day before, "Tomorrow" if it's one day ahead,
     * and otherwise, it delegates the formatting to the provided [GetDateFromOffsetDateTime] instance.

     * **Note:** Currently, relative terms ("Today", "Yesterday", "Tomorrow") are not formatted based on the provided locale.

     * @param offsetDateTime The `OffsetDateTime` to be converted.
     * @param locale The locale to use for formatting the date (partially used for future i18n).
     * @return The textual representation of the `offsetDateTime` relative to today.
     */
    operator fun invoke(offsetDateTime: OffsetDateTime, locale: Locale): String {
        val today = LocalDate.now(zoneId)
        val incoming = offsetDateTime.toInstant().atZone(zoneId).toLocalDate()

        val daysDiff = ChronoUnit.DAYS.between(incoming, today)
        return when (daysDiff) {
            0L -> "Today";
            1L -> "Yesterday";
            -1L -> "Tomorrow";
            else -> getDateFromOffsetDateTime(offsetDateTime, locale)
        }
    }
}