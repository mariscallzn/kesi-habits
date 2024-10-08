package com.kesicollection.database.impl.room.util

import androidx.room.TypeConverter
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

internal class OffsetDateTimeConverter {
    private val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME

    @TypeConverter
    fun toOffsetDateTime(value: String?): OffsetDateTime? =
        value?.let { OffsetDateTime.parse(value, formatter) }

    @TypeConverter
    fun fromOffsetDateTime(date: OffsetDateTime?): String? =
        date?.format(formatter)
}