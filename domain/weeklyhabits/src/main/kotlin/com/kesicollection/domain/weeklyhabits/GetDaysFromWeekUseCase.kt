package com.kesicollection.domain.weeklyhabits

import com.kesicollection.core.model.Day
import com.kesicollection.core.model.Week
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class GetDaysFromWeekUseCase @Inject constructor() {
    operator fun invoke(week: Week): List<Day> = week.days.map { offsetDateTime ->
        val formatterDayOfWeek = DateTimeFormatter.ofPattern("EEE")
        val formatterDayOfMonth = DateTimeFormatter.ofPattern("dd")
        Day(offsetDateTime.format(formatterDayOfWeek), offsetDateTime.format(formatterDayOfMonth))
    }
}