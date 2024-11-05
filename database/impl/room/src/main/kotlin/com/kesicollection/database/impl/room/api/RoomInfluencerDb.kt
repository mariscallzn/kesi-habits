package com.kesicollection.database.impl.room.api

import com.kesicollection.core.model.Influencer
import com.kesicollection.database.api.InfluencerDb
import com.kesicollection.database.impl.room.dao.InfluencerDao
import com.kesicollection.database.impl.room.model.toEntity
import com.kesicollection.database.impl.room.model.toInfluencer
import javax.inject.Inject

class RoomInfluencerDb @Inject constructor(
    private val influencerDao: InfluencerDao
) : InfluencerDb {
    override suspend fun insert(influencer: Influencer): Long =
        influencerDao.insert(influencer.toEntity())

    override suspend fun getAll(): List<Influencer> =
        influencerDao.getAll().map { it.toInfluencer() }
}