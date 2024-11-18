package com.kesicollection.core.model

/**
 * Represents a day with specific information.

 * This data class encapsulates the day of the week, day of the month, and the raw UTC `OffsetDateTime` in ISO 8601 format.

 * @property dayOfWeek The day of the week, formatted according to a specified locale.
 * @property dayOfMonth The day of the month as a string.
 * @property rawUTCDateTime The raw UTC `OffsetDateTime` in ISO 8601 format.
 */
data class Day(
    val dayOfWeek: String,
    val dayOfMonth: String,
    val rawUTCDateTime: String,
)