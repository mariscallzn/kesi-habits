package com.kesicollection.database.impl.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.kesicollection.database.impl.room.model.EmotionEntity

@Dao
interface EmotionDao : BaseDao<EmotionEntity> {

    @Query("SELECT * FROM emotions")
    suspend fun getAll(): List<EmotionEntity>

    @Query(
        """
        SELECT e.* FROM emotions e
        INNER JOIN entries_current_emotions ece ON e.id = ece.emotion_id
        WHERE ece.entry_id = :entryId
    """
    )
    suspend fun getCurrentEmotionsForEntry(entryId: String): List<EmotionEntity>

    @Query(
        """
        SELECT e.* FROM emotions e
        INNER JOIN entries_desire_emotions ede ON e.id = ede.emotion_id
        WHERE ede.entry_id = :entryId
    """
    )
    suspend fun getDesireEmotionsForEntry(entryId: String): List<EmotionEntity>
}