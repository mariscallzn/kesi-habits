package com.kesicollection.domain.datetime

import javax.inject.Inject

class GetTimeFromTimePicker @Inject constructor() {
    operator fun invoke(hour: Int, minute: Int, isAfternoon: Boolean): String {
        val formattedHour = if (isAfternoon && hour != 12) {
            hour - 12
        } else if (!isAfternoon && hour == 0) {
            12
        } else {
            hour
        }

        val formattedMinute = minute.toString().padStart(2, '0')
        val amPm = if (isAfternoon) "PM" else "AM"

        return "$formattedHour:$formattedMinute $amPm"
    }
}