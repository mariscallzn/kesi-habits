package com.kesicollection.data.entry

import com.kesicollection.core.model.Entry
import com.kesicollection.core.model.HabitType
import com.kesicollection.database.api.EntryDb
import javax.inject.Inject

interface EntryRepository {
    suspend fun addOrUpdateEntry(entries: List<Entry>)
    suspend fun add(entry: Entry): Long
    suspend fun updateHabit(entryId: String, habitId: String?, habitType: HabitType)
    suspend fun getById(id: String): Entry
}

class EntryRepositoryImpl @Inject constructor(
    private val entryDb: EntryDb,
) : EntryRepository {
    override suspend fun addOrUpdateEntry(entries: List<Entry>) = entryDb.upsertEntries(entries)

    override suspend fun add(entry: Entry): Long = entryDb.insert(entry)

    override suspend fun updateHabit(
        entryId: String,
        habitId: String?,
        habitType: HabitType
    ) {
        when (habitType) {
            HabitType.CORE -> entryDb.updateCoreHabit(entryId, habitId)
            HabitType.TRIGGER -> entryDb.updateTriggeredHabit(entryId, habitId)
        }
    }

    override suspend fun getById(id: String): Entry = entryDb.getById(id)
}