package com.kesicollection.data.habit

import com.kesicollection.core.model.Habit
import com.kesicollection.database.api.HabitDb
import java.util.UUID
import javax.inject.Inject

interface HabitRepository {
    suspend fun addOrUpdateHabits(habits: List<Habit>)
    suspend fun add(habit: Habit): Long
    suspend fun fetch(): List<Habit>
}

internal class HabitRepositoryImpl @Inject constructor(
    private val habitDb: HabitDb
) : HabitRepository {
    override suspend fun addOrUpdateHabits(habits: List<Habit>) = habitDb.upsertHabits(habits.map {
        if (it.id.isBlank()) it.copy(id = UUID.randomUUID().toString()) else it
    })

    override suspend fun add(habit: Habit) = habitDb.insert(habit)

    override suspend fun fetch(): List<Habit> = habitDb.getAll()
}