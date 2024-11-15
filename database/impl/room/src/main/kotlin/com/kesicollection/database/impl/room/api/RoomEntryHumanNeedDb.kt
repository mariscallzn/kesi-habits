package com.kesicollection.database.impl.room.api

import com.kesicollection.core.model.EntryHumanNeed
import com.kesicollection.database.api.EntryHumanNeedDb
import com.kesicollection.database.impl.room.dao.EntryHumanNeedDao
import com.kesicollection.database.impl.room.model.toEntity
import com.kesicollection.database.impl.room.model.toEntryHumanNeed
import javax.inject.Inject

class RoomEntryHumanNeedDb @Inject constructor(
    private val entryHumanNeedDao: EntryHumanNeedDao
) : EntryHumanNeedDb {
    override suspend fun insert(entryHumanNeed: EntryHumanNeed): Long =
        entryHumanNeedDao.insert(entryHumanNeed.toEntity())

    override suspend fun getEntryHumanNeedByEntryId(entryId: String): List<EntryHumanNeed> =
        entryHumanNeedDao.getHumanNeedsByEntryId(entryId).map { it.toEntryHumanNeed() }

    override suspend fun delete(id: Long) = entryHumanNeedDao.findById(id).run {
        entryHumanNeedDao.delete(this)
    }

    override suspend fun upsert(entryHumanNeed: List<EntryHumanNeed>) =
        entryHumanNeedDao.upsertAll(entryHumanNeed.map { it.toEntity() })

}