package com.kesicollection.domain.datetime

import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

/**
 * A class to format an `OffsetDateTime` object into its ISO 8601 string representation.

 * @Inject constructor()
 */
class GetIsoFormatFromOffsetDateTime @Inject constructor() {
    private val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME

    /**
     * Formats an `OffsetDateTime` object into its ISO 8601 string representation.

     * @param offsetDateTime The `OffsetDateTime` object to format.
     * @return The ISO 8601 formatted string representation of the `OffsetDateTime`.
     */
    operator fun invoke(offsetDateTime: OffsetDateTime): String =
        offsetDateTime.format(formatter)
}