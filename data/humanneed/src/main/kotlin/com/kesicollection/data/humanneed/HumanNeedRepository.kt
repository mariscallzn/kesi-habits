package com.kesicollection.data.humanneed

import com.kesicollection.core.model.HumanNeed
import com.kesicollection.database.api.HumanNeedDb
import javax.inject.Inject

interface HumanNeedRepository {
    suspend fun fetch(): List<HumanNeed>
}

internal class HumanNeedRepositoryImpl @Inject constructor(
    private val humanNeedDb: HumanNeedDb
) : HumanNeedRepository {
    override suspend fun fetch(): List<HumanNeed> = humanNeedDb.getAll()
}