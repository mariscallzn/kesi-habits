package com.kesicollection.domain.datetime

import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import javax.inject.Inject

/**
 * A use case that updates the time of an [OffsetDateTime] using the provided hour and minute,
 * while preserving the date and offset.
 *
 * This class is designed to be used with dependency injection, allowing the [ZoneId] to be
 * injected at runtime. The updated time is applied using the injected [ZoneId], but the
 * resulting [OffsetDateTime] will always have the UTC offset.
 *
 * @param zoneId The [ZoneId] to use for updating the time.
 */
class UpdateOffsetDateTimeWithTimePicker @Inject constructor(
    private val zoneId: ZoneId
) {

    /**
     * Updates the time of the given [OffsetDateTime] with the specified hour and minute.
     *
     * The date and offset of the original [OffsetDateTime] are preserved. The updated time is
     * applied using the injected [ZoneId], but the resulting [OffsetDateTime] will always have
     * the UTC offset.
     *
     * @param originalDateTime The original [OffsetDateTime] to update.
     * @param hour The hour to set in the updated [OffsetDateTime].
     * @param minute The minute to set in the updated [OffsetDateTime].
     * @return The updated [OffsetDateTime] with the new time and UTC offset.
     */
    operator fun invoke(
        originalDateTime: OffsetDateTime,
        hour: Int,
        minute: Int
    ): OffsetDateTime =
        originalDateTime.toInstant().atZone(zoneId).withHour(hour).withMinute(minute).toInstant()
            .atOffset(
                ZoneOffset.UTC
            )
}