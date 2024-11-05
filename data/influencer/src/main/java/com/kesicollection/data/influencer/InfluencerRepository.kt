package com.kesicollection.data.influencer

import com.kesicollection.core.model.Influencer
import com.kesicollection.database.api.InfluencerDb
import javax.inject.Inject

interface InfluencerRepository {
    suspend fun add(influencer: Influencer): Long
    suspend fun fetch(): List<Influencer>
}

internal class InfluencerRepositoryImpl @Inject constructor(
    private val influencerDb: InfluencerDb
) : InfluencerRepository {
    override suspend fun add(influencer: Influencer) = influencerDb.insert(influencer)

    override suspend fun fetch(): List<Influencer> = influencerDb.getAll()

}