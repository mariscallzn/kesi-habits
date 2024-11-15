package com.kesicollection.database.impl.room.api

import com.kesicollection.core.model.HumanNeed
import com.kesicollection.database.api.HumanNeedDb
import com.kesicollection.database.impl.room.dao.HumanNeedDao
import com.kesicollection.database.impl.room.model.HumanNeedEntity
import com.kesicollection.database.impl.room.model.toHumanNeed
import javax.inject.Inject

class RoomHumanNeedDb @Inject constructor(
    private val humanNeedDao: HumanNeedDao
) : HumanNeedDb {

    override suspend fun getAll(): List<HumanNeed> {
        val humanNeeds = humanNeedDao.getAll()
        return if (humanNeeds.isEmpty()) {
            humanNeedDao.insertAll(
                listOf(
                    HumanNeedEntity(name = "Certainty", i18Key = "i18_certainty"),
                    HumanNeedEntity(name = "Variety", i18Key = "i18_variety"),
                    HumanNeedEntity(name = "Significance", i18Key = "i18_significance"),
                    HumanNeedEntity(name = "Love / Connection", i18Key = "i18_love_connection"),
                    HumanNeedEntity(name = "Growth", i18Key = "i18_growth"),
                    HumanNeedEntity(name = "Contribution", i18Key = "i18_contribution"),
                )
            )
            humanNeedDao.getAll().map { it.toHumanNeed() }
        } else humanNeeds.map { it.toHumanNeed() }
    }

    override suspend fun findById(id: Int): HumanNeed =
        humanNeedDao.findById(id).toHumanNeed()
}