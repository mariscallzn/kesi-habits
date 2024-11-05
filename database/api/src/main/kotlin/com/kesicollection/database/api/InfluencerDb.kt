package com.kesicollection.database.api

import com.kesicollection.core.model.Influencer

interface InfluencerDb {
    suspend fun insert(influencer: Influencer): Long
    suspend fun getAll(): List<Influencer>
}