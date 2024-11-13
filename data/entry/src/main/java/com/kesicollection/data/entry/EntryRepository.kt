package com.kesicollection.data.entry

import com.kesicollection.core.model.EmotionType
import com.kesicollection.core.model.Entry
import com.kesicollection.core.model.EntryEmotion
import com.kesicollection.core.model.EntryInfluencer
import com.kesicollection.core.model.HabitType
import com.kesicollection.core.model.Status
import com.kesicollection.database.api.EntryDb
import com.kesicollection.database.api.EntryEmotionDb
import com.kesicollection.database.api.EntryInfluencerDb
import com.kesicollection.domain.datetime.UpdateOffsetDateTimeWithMillis
import com.kesicollection.domain.datetime.UpdateOffsetDateTimeWithTimePicker
import javax.inject.Inject

interface EntryRepository {
    suspend fun addOrUpdateEntry(entries: List<Entry>)
    suspend fun add(entry: Entry): Long
    suspend fun updateHabit(
        entryId: String,
        habitId: String?,
        habitType: HabitType?,
        emotionIds: List<String>,
        emotionType: EmotionType?,
        influencersIds: List<String>?
    )

    suspend fun getById(id: String): Entry
    suspend fun updateEntryStatus(id: String, status: Status): Entry
    suspend fun updateDate(id: String, millis: Long?)
    suspend fun updateTime(
        id: String, hour: Int,
        minute: Int
    )
}

internal class EntryRepositoryImpl @Inject constructor(
    private val entryDb: EntryDb,
    private val entryEmotionDb: EntryEmotionDb,
    private val entryInfluencerDb: EntryInfluencerDb,
    private val updateOffsetDateTimeWithMillis: UpdateOffsetDateTimeWithMillis,
    private val updateOffsetDateTimeWithTimePicker: UpdateOffsetDateTimeWithTimePicker,
) : EntryRepository {
    override suspend fun addOrUpdateEntry(entries: List<Entry>) = entryDb.upsertEntries(entries)

    override suspend fun add(entry: Entry): Long = entryDb.insert(entry)

    override suspend fun updateHabit(
        entryId: String,
        habitId: String?,
        habitType: HabitType?,
        emotionIds: List<String>,
        emotionType: EmotionType?,
        influencersIds: List<String>?
    ) {
        when {
            habitType != null -> {
                entryDb.updateHabit(entryId, habitId, habitType)
            }

            emotionType != null -> {
                val allEmotionsByEntry =
                    entryEmotionDb.getEntryEmotionByEntryIdAndType(entryId, emotionType)

                val emotionsSet = emotionIds.toSet()
                val allEmotionsSet = allEmotionsByEntry.map { it.emotionId }.toSet()

                allEmotionsByEntry.filterNot { emotionsSet.contains(it.emotionId) }.forEach {
                    entryEmotionDb.delete(it.id)
                }

                emotionIds.filterNot { allEmotionsSet.contains(it) }.forEach {
                    entryEmotionDb.insert(
                        EntryEmotion(
                            entryId = entryId,
                            emotionId = it,
                            emotionType = emotionType
                        )
                    )
                }
            }

            influencersIds != null -> {
                val allEntryInfluencers = entryInfluencerDb.getEntryInfluencerByEntryId(entryId)

                val influencersSet = influencersIds.toSet()
                val allEntryInfluencersSet = allEntryInfluencers.map { it.influencerId }.toSet()

                allEntryInfluencers.filterNot { influencersSet.contains(it.influencerId) }.forEach {
                    entryInfluencerDb.delete(it.id)
                }

                influencersIds.filterNot { allEntryInfluencersSet.contains(it) }.forEach {
                    entryInfluencerDb.insert(
                        EntryInfluencer(
                            entryId = entryId,
                            influencerId = it
                        )
                    )
                }
            }
        }
    }

    override suspend fun getById(id: String): Entry = entryDb.getById(id)

    override suspend fun updateEntryStatus(id: String, status: Status): Entry {
        val entry = getById(id).copy(status = status)
        entryDb.update(entry)
        return entry
    }

    override suspend fun updateDate(id: String, millis: Long?) {
        millis?.let {
            val entry = getById(id)
            val updatedDate = updateOffsetDateTimeWithMillis(entry.recordedOn, millis)
            entryDb.update(entry.copy(recordedOn = updatedDate))
        }
    }

    override suspend fun updateTime(id: String, hour: Int, minute: Int) {
        val entry = getById(id)
        val updatedTime =
            updateOffsetDateTimeWithTimePicker(entry.recordedOn, hour, minute)
        entryDb.update(entry.copy(recordedOn = updatedTime))
    }

}