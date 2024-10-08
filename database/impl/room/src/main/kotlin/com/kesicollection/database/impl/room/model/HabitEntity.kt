package com.kesicollection.database.impl.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kesicollection.core.model.Habit
import java.time.OffsetDateTime
import java.time.ZoneOffset

@Entity(
    tableName = "habits"
)
data class HabitEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    @ColumnInfo(name = "recorded_on")
    val recordedOn: OffsetDateTime = OffsetDateTime.now(ZoneOffset.UTC)
)

fun Habit.toEntity(): HabitEntity =
    HabitEntity(
        id = id,
        title = title,
        //TODO: Think on this
        recordedOn = OffsetDateTime.now()
    )