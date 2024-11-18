package com.kesicollection.domain.datetime

import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import javax.inject.Inject

/**
 * Calculates the start and end `OffsetDateTime` for a given `OffsetDateTime` within a specified time range.

 * This class takes an `OffsetDateTime` and optional start and end times, and calculates the
 * corresponding start and end `OffsetDateTime` within the specified time range. It ensures that the
 * start and end times are within valid ranges (0-23).

 * @param zoneId The target time zone to convert the `OffsetDateTime` to.
 */
class GetStartEndFromOffsetDateTime @Inject constructor(
    private val zoneId: ZoneId,
) {

    /**
     * Calculates the start and end `OffsetDateTime` for a given `OffsetDateTime`.

     * @param datetime The `OffsetDateTime` to calculate the range for.
     * @param startFrom The starting hour of the range (inclusive, default: 0).
     * @param endFrom The ending hour of the range (inclusive, default: 23).

     * @return A pair of `OffsetDateTime` objects representing the start and end of the range.
     */
    operator fun invoke(
        datetime: OffsetDateTime,
        startFrom: Int = 0,
        endFrom: Int = 24
    ): Pair<OffsetDateTime, OffsetDateTime> {

        val zonedDateTime = datetime.toInstant().atZone(zoneId)

        // Ensure startFrom and endFrom are within valid range (0-23)
        val clampedStartFrom = startFrom.coerceIn(0, 23)
        val clampedEndFrom = endFrom.coerceIn(clampedStartFrom, 23)

        // Calculate the start and end OffsetDateTime
        val start = zonedDateTime.withHour(clampedStartFrom).withMinute(0).withSecond(0).withNano(0)
            .withZoneSameInstant(
                ZoneOffset.UTC
            )
        val end =
            zonedDateTime.withHour(clampedEndFrom).withMinute(59).withSecond(59).withNano(999999999)
                .withZoneSameInstant(ZoneOffset.UTC)

        return Pair(start.toOffsetDateTime(), end.toOffsetDateTime())
    }
}