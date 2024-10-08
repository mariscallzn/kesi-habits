package com.kesicollection.database.api

import com.kesicollection.core.model.Habit

interface HabitDb {
    suspend fun upsertHabits(habits: List<Habit>)
}