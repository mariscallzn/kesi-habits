package com.kesicollection.database.impl.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.kesicollection.database.impl.room.model.InfluencerEntity

@Dao
interface InfluencerDao : BaseDao<InfluencerEntity> {
    @Query("SELECT * FROM influencers")
    suspend fun getAll(): List<InfluencerEntity>

    @Query(
        """
            SELECT i.* FROM influencers i 
            INNER JOIN entries_influencers ei ON i.id = ei.influencer_id 
            WHERE ei.entry_id = :entryId
    """
    )
    suspend fun getInfluencersForEntry(entryId: String): List<InfluencerEntity>

}