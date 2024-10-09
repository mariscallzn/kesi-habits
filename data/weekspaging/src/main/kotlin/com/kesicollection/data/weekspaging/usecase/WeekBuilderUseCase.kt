package com.kesicollection.data.weekspaging.usecase

import com.kesicollection.core.model.Week
import java.time.OffsetDateTime
import java.time.ZoneOffset
import javax.inject.Inject

class WeekBuilderUseCase @Inject constructor() {
    operator fun invoke(
        from: OffsetDateTime = OffsetDateTime.now(ZoneOffset.UTC),
        total: Long = 1
    ): List<Week> {
        val weeksLater = from.plusWeeks(total)
        val weeks = mutableListOf<Week>()
        var currentWeek = from
        while (currentWeek < weeksLater) {
            val days = mutableListOf<OffsetDateTime>()
            days.add(currentWeek)
            for (i in 1..6) {
                days.add(currentWeek.plusDays(i.toLong()))
            }
            weeks.add(Week(days))
            currentWeek = currentWeek.plusWeeks(1)
        }
        return weeks
    }
}