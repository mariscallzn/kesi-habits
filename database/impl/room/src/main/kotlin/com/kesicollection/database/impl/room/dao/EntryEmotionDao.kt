package com.kesicollection.database.impl.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.kesicollection.core.model.EmotionType
import com.kesicollection.database.impl.room.model.EntryEmotionCrossRef

@Dao
interface EntryEmotionDao : BaseDao<EntryEmotionCrossRef> {

    @Query(
        """
        SELECT * FROM entries_emotions
        WHERE id = :id
    """
    )
    suspend fun findById(id: Long): EntryEmotionCrossRef

    @Query(
        """
        SELECT * FROM entries_emotions
        WHERE entry_id = :id AND emotion_type = :type
    """
    )
    suspend fun getEntryEmotionByEntryIdAndEmotionType(id: String, type: EmotionType): List<EntryEmotionCrossRef>
}