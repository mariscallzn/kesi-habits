package com.kesicollection.database.impl.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.ForeignKey.Companion.SET_NULL
import androidx.room.PrimaryKey
import com.kesicollection.core.model.Entry
import com.kesicollection.core.model.Status
import java.time.OffsetDateTime

@Entity(
    tableName = "entries",
    foreignKeys = [
        ForeignKey(
            entity = HabitEntity::class,
            parentColumns = ["id"],
            childColumns = ["habit_id"],
            onDelete = CASCADE
        ),
        ForeignKey(
            entity = HabitEntity::class,
            parentColumns = ["id"],
            childColumns = ["triggered_by_habit_id"],
            onDelete = SET_NULL
        )]
)
data class EntryEntity(
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "habit_id")
    val habitId: String,
    @ColumnInfo(name = "recorded_on")
    val recordedOn: OffsetDateTime,
    @ColumnInfo(name = "triggered_by_habit_id")
    val triggeredByHabitId: String?,
    val status: Status,
)

fun Entry.toEntity(): EntryEntity = EntryEntity(
    id = id,
    habitId = habit.id,
    recordedOn = recordedOn,
    triggeredByHabitId = triggeredBy?.id,
    status = status
)