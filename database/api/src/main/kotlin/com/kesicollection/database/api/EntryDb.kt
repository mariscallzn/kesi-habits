package com.kesicollection.database.api

import com.kesicollection.core.model.Entry

interface EntryDb {
    suspend fun upsertEntries(entries: List<Entry>)
    suspend fun insert(entry: Entry): Long
    suspend fun updateCoreHabit(entryId: String, habitId: String?)
    suspend fun updateTriggeredHabit(entryId: String, habitId: String?)
    suspend fun getById(id: String): Entry
}