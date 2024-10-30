package com.kesicollection.data.entry

import com.kesicollection.core.model.EmotionType
import com.kesicollection.core.model.Entry
import com.kesicollection.core.model.EntryEmotion
import com.kesicollection.core.model.HabitType
import com.kesicollection.database.api.EntryDb
import com.kesicollection.database.api.EntryEmotionDb
import javax.inject.Inject

interface EntryRepository {
    suspend fun addOrUpdateEntry(entries: List<Entry>)
    suspend fun add(entry: Entry): Long
    suspend fun updateHabit(
        entryId: String,
        habitId: String?,
        habitType: HabitType?,
        emotionId: String?,
        emotionType: EmotionType?
    )

    suspend fun getById(id: String): Entry
}

class EntryRepositoryImpl @Inject constructor(
    private val entryDb: EntryDb,
    private val entryEmotionDb: EntryEmotionDb,
) : EntryRepository {
    override suspend fun addOrUpdateEntry(entries: List<Entry>) = entryDb.upsertEntries(entries)

    override suspend fun add(entry: Entry): Long = entryDb.insert(entry)

    override suspend fun updateHabit(
        entryId: String,
        habitId: String?,
        habitType: HabitType?,
        emotionId: String?,
        emotionType: EmotionType?
    ) {
        when {
            habitType != null ->
                entryDb.updateHabit(entryId, habitId, habitType)

            emotionId != null && emotionType != null ->
                entryEmotionDb.insert(EntryEmotion(entryId, emotionId), emotionType)
        }
    }

    override suspend fun getById(id: String): Entry = entryDb.getById(id)
}