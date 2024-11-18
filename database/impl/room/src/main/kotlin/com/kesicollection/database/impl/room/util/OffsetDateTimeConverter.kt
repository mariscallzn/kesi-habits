package com.kesicollection.database.impl.room.util

import androidx.room.TypeConverter
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

/**
 * A converter class for converting between `OffsetDateTime` and its ISO 8601 string representation.

 * This class is used for Room database type converters to store and retrieve `OffsetDateTime` values as strings.
 */
internal class OffsetDateTimeConverter {
    private val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME

    /**
     * Converts an ISO 8601 formatted string to an `OffsetDateTime` object.

     * @param value The ISO 8601 formatted string to parse.
     * @return The parsed `OffsetDateTime` object, or null if the input string is null or invalid.
     */
    @TypeConverter
    fun toOffsetDateTime(value: String?): OffsetDateTime? =
        value?.let { OffsetDateTime.parse(value, formatter) }

    /**
     * Converts an `OffsetDateTime` object to its ISO 8601 string representation.

     * @param date The `OffsetDateTime` object to convert.
     * @return The ISO 8601 formatted string representation of the date, or null if the input date is null.
     */
    @TypeConverter
    fun fromOffsetDateTime(date: OffsetDateTime?): String? =
        date?.format(formatter)
}