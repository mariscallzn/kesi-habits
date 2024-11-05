package com.kesicollection.database.api

import com.kesicollection.core.model.EntryInfluencer

interface EntryInfluencerDb {
    suspend fun insert(entryInfluencer: EntryInfluencer): Long
    suspend fun getEntryInfluencerByEntryId(entryId: String): List<EntryInfluencer>
    suspend fun delete(id: Long)
}