package com.kesicollection.database.impl.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.kesicollection.database.impl.room.model.InfluencerEntity

@Dao
abstract class InfluencerDao : BaseDao<InfluencerEntity> {
    @Query("SELECT * FROM influencers")
    abstract suspend fun getAll(): List<InfluencerEntity>
}