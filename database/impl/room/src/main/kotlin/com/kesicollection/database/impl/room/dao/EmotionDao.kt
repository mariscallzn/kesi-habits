package com.kesicollection.database.impl.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.kesicollection.core.model.EmotionType
import com.kesicollection.database.impl.room.model.EmotionEntity

@Dao
interface EmotionDao : BaseDao<EmotionEntity> {

    @Query("SELECT * FROM emotions")
    suspend fun getAll(): List<EmotionEntity>

    @Query(
        """
        SELECT e.* FROM emotions e
        INNER JOIN entries_emotions ee ON e.id = ee.emotion_id
        WHERE ee.entry_id = :entryId AND ee.emotion_type = :emotionType
    """
    )
    suspend fun getEmotionsByEntryIdAndEmotionType(
        entryId: String,
        emotionType: EmotionType
    ): List<EmotionEntity>
}