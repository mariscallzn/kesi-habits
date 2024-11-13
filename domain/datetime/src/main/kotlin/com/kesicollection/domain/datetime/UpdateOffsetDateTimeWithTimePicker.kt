package com.kesicollection.domain.datetime

import java.time.LocalTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import javax.inject.Inject

class UpdateOffsetDateTimeWithTimePicker @Inject constructor(
    private val zoneId: ZoneId
) {
    operator fun invoke(
        originalDateTime: OffsetDateTime,
        hour: Int,
        minute: Int
    ): OffsetDateTime {
        val localTime = LocalTime.of(hour, minute) // Extract time

        // Combine date from originalDateTime (in UTC) with localTime
        val combinedDateTime = originalDateTime.toInstant().atZone(zoneId).toLocalDate().atTime(localTime)

        // Convert back to OffsetDateTime in UTC
        return combinedDateTime.atZone(zoneId).toInstant().atOffset(ZoneOffset.UTC)

    }
}