package com.kesicollection.database.impl.room.api

import com.kesicollection.core.model.EntryInfluencer
import com.kesicollection.database.api.EntryInfluencerDb
import com.kesicollection.database.impl.room.dao.EntryInfluencerDao
import com.kesicollection.database.impl.room.model.toEntity
import com.kesicollection.database.impl.room.model.toEntryInfluencer
import javax.inject.Inject

class RoomEntryInfluencerDb @Inject constructor(
    private val entryInfluencerDao: EntryInfluencerDao
) : EntryInfluencerDb {
    override suspend fun insert(entryInfluencer: EntryInfluencer): Long =
        entryInfluencerDao.insert(entryInfluencer.toEntity())

    override suspend fun getEntryInfluencerByEntryId(entryId: String): List<EntryInfluencer> =
        entryInfluencerDao.getInfluencersByEntryId(entryId).map { it.toEntryInfluencer() }

    override suspend fun delete(id: Long) = entryInfluencerDao.findById(id).run {
        entryInfluencerDao.delete(this)
    }

}