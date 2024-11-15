package com.kesicollection.data.entry

import com.kesicollection.core.model.Entry
import com.kesicollection.core.model.EntryHumanNeed
import com.kesicollection.core.model.EntryInfluencer
import com.kesicollection.core.model.HabitType
import com.kesicollection.core.model.HumanNeed
import com.kesicollection.core.model.Status
import com.kesicollection.database.api.EntryDb
import com.kesicollection.database.api.EntryHumanNeedDb
import com.kesicollection.database.api.EntryInfluencerDb
import com.kesicollection.database.api.HumanNeedDb
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
        influencersIds: List<String>?
    )

    suspend fun getById(id: String): Entry
    suspend fun updateEntryStatus(id: String, status: Status): Entry
    suspend fun updateDate(id: String, millis: Long?)
    suspend fun updateTime(
        id: String, hour: Int,
        minute: Int
    )

    suspend fun updateHumanNeeds(id: String, humanNeeds: List<HumanNeed>)
}

internal class EntryRepositoryImpl @Inject constructor(
    private val entryDb: EntryDb,
    private val entryInfluencerDb: EntryInfluencerDb,
    private val entryHumanNeedDb: EntryHumanNeedDb,
    private val humanNeedDb: HumanNeedDb,
    private val updateOffsetDateTimeWithMillis: UpdateOffsetDateTimeWithMillis,
    private val updateOffsetDateTimeWithTimePicker: UpdateOffsetDateTimeWithTimePicker,
) : EntryRepository {
    override suspend fun addOrUpdateEntry(entries: List<Entry>) = entryDb.upsertEntries(entries)

    override suspend fun add(entry: Entry): Long = entryDb.insert(entry)

    override suspend fun updateHabit(
        entryId: String,
        habitId: String?,
        habitType: HabitType?,
        influencersIds: List<String>?
    ) {
        when {
            habitType != null -> {
                entryDb.updateHabit(entryId, habitId, habitType)
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

    override suspend fun getById(id: String): Entry =
        entryDb.getById(id).copy(humanNeeds = entryHumanNeedDb.getEntryHumanNeedByEntryId(id).map {
            humanNeedDb.findById(it.humanNeedId).copy(ranked = it.rank)
        })

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

    override suspend fun updateHumanNeeds(id: String, humanNeeds: List<HumanNeed>) {
        val dbItems = entryHumanNeedDb.getEntryHumanNeedByEntryId(id)
        val updatedNeeds = humanNeeds.map {
            EntryHumanNeed(
                dbItems.find { item -> item.entryId == id && item.humanNeedId == it.id }?.id ?: 0,
                id,
                it.id,
                it.ranked
            )
        }
        entryHumanNeedDb.upsert(updatedNeeds)
    }

}