package com.kesicollection.database.api

import com.kesicollection.core.model.HumanNeed

interface HumanNeedDb {
    suspend fun getAll(): List<HumanNeed>
    suspend fun findById(id: Int): HumanNeed
}