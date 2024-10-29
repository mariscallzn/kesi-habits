package com.kesicollection.database.api

import com.kesicollection.core.model.Habit

interface HabitDb {
    suspend fun upsertHabits(habits: List<Habit>)
    suspend fun insert(habit: Habit): Long
    suspend fun findById(id: String): Habit
}