package com.kesicollection.database.impl.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.kesicollection.database.impl.room.model.EntryEntity
import com.kesicollection.database.impl.room.model.EntryHabitJoin

@Dao
interface EntryDao : BaseDao<EntryEntity> {

    @Query(
        """
        SELECT * FROM entries
        WHERE id = :id
    """
    )
    suspend fun findById(id: String): EntryEntity

    @Query(
        """
        SELECT e.id, e.recorded_on AS recordedOn, e.status, h.id AS habitId, h.name AS habitName, h.classification AS habitClassification, h.status AS habitStatus, t.id AS triggeredHabitId, t.name AS triggeredHabitName, t.classification AS triggeredHabitClassification, t.status AS triggeredHabitStatus  
        FROM entries e
        LEFT JOIN habits h ON e.habit_id = h.id 
        LEFT JOIN habits t ON e.triggered_by_habit_id = t.id
        WHERE e.id = :id
    """
    )
    suspend fun findEntryHabitById(id: String): EntryHabitJoin


}