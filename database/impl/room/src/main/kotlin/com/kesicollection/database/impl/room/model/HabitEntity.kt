package com.kesicollection.database.impl.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kesicollection.core.model.Classification
import com.kesicollection.core.model.Habit
import com.kesicollection.core.model.Status

@Entity(
    tableName = "habits"
)
data class HabitEntity(
    @PrimaryKey
    val id: String,
    @ColumnInfo(index = true)
    val name: String,
    val classification: Classification,
    val status: Status,
)

fun Habit.toEntity(): HabitEntity = HabitEntity(
    id = id,
    name = name,
    classification = classification,
    status = status,
)

fun HabitEntity.toHabit(): Habit = Habit(
    id, name, classification, status
)