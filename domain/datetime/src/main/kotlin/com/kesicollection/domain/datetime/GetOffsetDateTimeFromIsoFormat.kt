package com.kesicollection.domain.datetime

import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import javax.inject.Inject

/**
 * A class to parse an ISO 8601 formatted string into an `OffsetDateTime` object.

 * @Inject constructor()
 */
class GetOffsetDateTimeFromIsoFormat @Inject constructor() {
    private val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME

    /**
     * Parses an ISO 8601 formatted string into an `OffsetDateTime` object.

     * @param value The ISO 8601 formatted string to parse.
     * @return The parsed `OffsetDateTime` object.
     * @throws DateTimeParseException If the input string is not a valid ISO 8601 format.
     */
    operator fun invoke(value: String): OffsetDateTime = OffsetDateTime.parse(value, formatter)
}