package com.kesicollection.database.impl.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.kesicollection.database.impl.room.model.HabitEntity

@Dao
interface HabitDao : BaseDao<HabitEntity> {

    @Query(
        """
        SELECT * FROM habits
        WHERE id = :id
    """
    )
    suspend fun findById(id: String): HabitEntity

    @Query("SELECT * FROM habits")
    suspend fun getAll(): List<HabitEntity>
}