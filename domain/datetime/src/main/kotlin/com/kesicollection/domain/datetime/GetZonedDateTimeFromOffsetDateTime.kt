package com.kesicollection.domain.datetime

import java.time.OffsetDateTime
import java.time.ZoneId
import javax.inject.Inject

/**
 * A use case that converts an [OffsetDateTime] to a ZonedDateTime using the provided [ZoneId].
 *
 * This class is designed to be used with dependency injection, allowing the [ZoneId] to be
 * injected at runtime.
 *
 * @param zoneId The [ZoneId] to use for the conversion.
 */
class GetZonedDateTimeFromOffsetDateTime @Inject constructor(
    private val zoneId: ZoneId,
) {

    /**
     * Converts the given [OffsetDateTime] to a ZonedDateTime using the injected [ZoneId].
     *
     * @param offsetDateTime The [OffsetDateTime] to convert.
     * @return The converted [OffsetDateTime].
     */
    operator fun invoke(offsetDateTime: OffsetDateTime): OffsetDateTime =
        offsetDateTime.toInstant().atZone(zoneId).toOffsetDateTime()
}