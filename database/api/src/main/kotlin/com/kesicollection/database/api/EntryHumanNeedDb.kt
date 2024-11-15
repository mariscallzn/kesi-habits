package com.kesicollection.database.api

import com.kesicollection.core.model.EntryHumanNeed

interface EntryHumanNeedDb {
    suspend fun insert(entryHumanNeed: EntryHumanNeed): Long
    suspend fun getEntryHumanNeedByEntryId(entryId: String): List<EntryHumanNeed>
    suspend fun delete(id: Long)
    suspend fun upsert(entryHumanNeed: List<EntryHumanNeed>)
}