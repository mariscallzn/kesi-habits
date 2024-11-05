package com.kesicollection.database.impl.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.kesicollection.database.impl.room.model.EntryInfluencerCrossRef

@Dao
interface EntryInfluencerDao : BaseDao<EntryInfluencerCrossRef> {

    @Query(
        """
        SELECT * FROM entries_influencers
        WHERE id = :id
    """
    )
    suspend fun findById(id: Long): EntryInfluencerCrossRef

    @Query(
        """
        SELECT * FROM entries_influencers
        WHERE entry_id = :entryId
    """
    )
    suspend fun getInfluencersByEntryId(entryId: String): List<EntryInfluencerCrossRef>
}