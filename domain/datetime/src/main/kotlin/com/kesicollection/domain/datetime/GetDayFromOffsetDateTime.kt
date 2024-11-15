package com.kesicollection.domain.datetime

import com.kesicollection.core.model.Day
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.TextStyle
import java.util.Locale
import javax.inject.Inject

class GetDayFromOffsetDateTime @Inject constructor(
    private val zoneId: ZoneId
) {
    operator fun invoke(offsetDateTime: OffsetDateTime, locale: Locale): Day {
        val zonedDateTime = offsetDateTime.atZoneSameInstant(zoneId)
        val dayOfWeek = zonedDateTime.dayOfWeek.getDisplayName(TextStyle.SHORT, locale)
        val dayOfMonth = zonedDateTime.dayOfMonth

        return Day(dayOfWeek = dayOfWeek, dayOfMonth = dayOfMonth.toString())
    }
}