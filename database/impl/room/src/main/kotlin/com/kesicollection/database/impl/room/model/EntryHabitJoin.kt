package com.kesicollection.database.impl.room.model

import com.kesicollection.core.model.Classification
import com.kesicollection.core.model.Entry
import com.kesicollection.core.model.Habit
import com.kesicollection.core.model.Status
import java.time.OffsetDateTime

data class EntryHabitJoin(
    val id: String,
    val recordedOn: OffsetDateTime,
    val status: Status,
    val habitId: String? = null,
    val habitName: String? = null,
    val habitClassification: Classification? = null,
    val habitStatus: Status? = null,
    val triggeredHabitId: String? = null,
    val triggeredHabitName: String? = null,
    val triggeredHabitClassification: Classification? = null,
    val triggeredHabitStatus: Status? = null,
)

fun EntryHabitJoin.toEntry(): Entry {
    val habit =
        if (habitId != null && habitName != null && habitClassification != null && habitStatus != null) {
            Habit(habitId, habitName, habitClassification, habitStatus)
        } else null

    val triggerHabit =
        if (triggeredHabitId != null && triggeredHabitName != null && triggeredHabitClassification != null && triggeredHabitStatus != null) {
            Habit(
                triggeredHabitId,
                triggeredHabitName,
                triggeredHabitClassification,
                triggeredHabitStatus
            )
        } else null

    return Entry(id, recordedOn, status, habit, triggerHabit)
}
