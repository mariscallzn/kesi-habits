package com.kesicollection.database.impl.room.api

import com.kesicollection.core.model.Entry
import com.kesicollection.core.model.HabitType
import com.kesicollection.database.api.EntryDb
import com.kesicollection.database.impl.room.dao.EmotionDao
import com.kesicollection.database.impl.room.dao.EntryDao
import com.kesicollection.database.impl.room.dao.InfluencerDao
import com.kesicollection.database.impl.room.model.toEmotion
import com.kesicollection.database.impl.room.model.toEntity
import com.kesicollection.database.impl.room.model.toEntry
import com.kesicollection.database.impl.room.model.toInfluencer
import javax.inject.Inject

class RoomEntryDb @Inject constructor(
    private val entryDao: EntryDao,
    private val influencerDao: InfluencerDao,
    private val emotionDao: EmotionDao
) : EntryDb {
    override suspend fun upsertEntries(entries: List<Entry>) =
        entryDao.upsertAll(entries.map { it.toEntity() })

    override suspend fun insert(entry: Entry): Long =
        entryDao.insert(entry.toEntity())

    override suspend fun updateHabit(entryId: String, habitId: String?, habitType: HabitType) {
        val entry = entryDao.findById(entryId)
        val updated = when (habitType) {
            HabitType.CORE -> entry.copy(habitId = habitId)
            HabitType.TRIGGER -> entry.copy(triggeredByHabitId = habitId)
        }
        entryDao.update(updated)
    }

    override suspend fun getById(id: String): Entry {
        return entryDao.findEntryHabitById(id).toEntry()
            .copy(
                influencers = influencerDao.getInfluencersForEntry(id).map { it.toInfluencer() },
                currentEmotions = emotionDao.getCurrentEmotionsForEntry(id).map { it.toEmotion() },
                desiredEmotions = emotionDao.getDesireEmotionsForEntry(id).map { it.toEmotion() }
            )
    }
}