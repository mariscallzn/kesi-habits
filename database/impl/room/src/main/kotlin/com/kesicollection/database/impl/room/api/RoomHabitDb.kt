package com.kesicollection.database.impl.room.api

import com.kesicollection.core.model.Habit
import com.kesicollection.database.api.HabitDb
import com.kesicollection.database.impl.room.dao.HabitDao
import com.kesicollection.database.impl.room.model.toEntity
import javax.inject.Inject

class RoomHabitDb @Inject constructor(
    private val dao: HabitDao
) : HabitDb {
    override suspend fun upsertHabits(habits: List<Habit>) =
        dao.upsertAll(habits.map { it.toEntity() })
}