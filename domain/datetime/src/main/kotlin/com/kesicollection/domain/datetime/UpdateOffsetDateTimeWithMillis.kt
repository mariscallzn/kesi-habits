package com.kesicollection.domain.datetime

import java.time.Instant
import java.time.OffsetDateTime
import javax.inject.Inject

class UpdateOffsetDateTimeWithMillis @Inject constructor() {
    operator fun invoke(originalDateTime: OffsetDateTime, newDateMillis: Long): OffsetDateTime {
        // Create an Instant from the new date milliseconds
        val newDateInstant = Instant.ofEpochMilli(newDateMillis)

        // Extract the time component from the original OffsetDateTime
        val originalTime = originalDateTime.toLocalTime()

        // Combine the new date Instant with the original time, preserving the offset
        val newDateTime = OffsetDateTime.ofInstant(newDateInstant, originalDateTime.offset)
            .withHour(originalTime.hour)
            .withMinute(originalTime.minute)
            .withSecond(originalTime.second)
            .withNano(originalTime.nano)

        return newDateTime
    }
}