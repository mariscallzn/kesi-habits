package com.kesicollection.domain.datetime

import java.time.OffsetDateTime
import java.time.ZoneId
import javax.inject.Inject

class GetTimePairFromOffsetDateTime @Inject constructor(private val zoneId: ZoneId) {
    operator fun invoke(offsetDateTime: OffsetDateTime): Pair<Int, Int> =
        offsetDateTime.atZoneSameInstant(zoneId).toLocalDateTime().run {
            hour to minute
        }
}
