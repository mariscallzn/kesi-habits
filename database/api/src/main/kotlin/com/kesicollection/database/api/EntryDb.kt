package com.kesicollection.database.api

import com.kesicollection.core.model.Entry
import com.kesicollection.core.model.HabitType

interface EntryDb {
    suspend fun upsertEntries(entries: List<Entry>)
    suspend fun insert(entry: Entry): Long
    suspend fun updateHabit(entryId: String, habitId: String?, type: HabitType)
    suspend fun getById(id: String): Entry
}