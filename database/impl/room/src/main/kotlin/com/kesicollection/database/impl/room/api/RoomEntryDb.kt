package com.kesicollection.database.impl.room.api

import com.kesicollection.core.model.Entry
import com.kesicollection.core.model.HabitType
import com.kesicollection.core.model.HumanNeed
import com.kesicollection.core.model.Influencer
import com.kesicollection.database.api.EntryDb
import com.kesicollection.database.impl.room.dao.EntryDao
import com.kesicollection.database.impl.room.dao.HumanNeedDao
import com.kesicollection.database.impl.room.dao.InfluencerDao
import com.kesicollection.database.impl.room.model.toEntity
import com.kesicollection.database.impl.room.model.toEntry
import com.kesicollection.database.impl.room.model.toHumanNeed
import com.kesicollection.database.impl.room.model.toInfluencer
import java.time.OffsetDateTime
import javax.inject.Inject

/**
 * Room database implementation of the [EntryDb] interface.
 *
 * This class provides data access functionalities for [Entry] objects using Room database.
 * It utilizes Data Access Objects (DAOs) - [EntryDao], [InfluencerDao], and [HumanNeedDao] -
 * to interact with the database.
 *
 * @param entryDao DAO for accessing [Entry] entities.
 * @param influencerDao DAO for accessing [Influencer] entities.
 * @param humanNeedDao DAO for accessing [HumanNeed] entities.
 */
class RoomEntryDb @Inject constructor(
    private val entryDao: EntryDao,
    private val influencerDao: InfluencerDao,
    private val humanNeedDao: HumanNeedDao
) : EntryDb {

    /**
     * Inserts or updates multiple [Entry] objects in the database.
     *
     * @param entries The list of [Entry] objects to upsert.
     */
    override suspend fun upsertEntries(entries: List<Entry>) =
        entryDao.upsertAll(entries.map { it.toEntity() })

    /**
     * Inserts a single [Entry] object into the database.
     *
     * @param entry The [Entry] object to insert.
     * @return The generated ID of the inserted [Entry].
     */
    override suspend fun insert(entry: Entry): Long =
        entryDao.insert(entry.toEntity())

    /**
     * Updates the habit ID of an existing [Entry].
     *
     * @param entryId The ID of the [Entry] to update.
     * @param habitId The new habit ID to set.
     * @param type The type of habit to update (CORE or TRIGGER).
     */
    override suspend fun updateHabit(entryId: String, habitId: String?, type: HabitType) {
        val entry = entryDao.findById(entryId)
        val updated = when (type) {
            HabitType.CORE -> entry.copy(habitId = habitId)
            HabitType.TRIGGER -> entry.copy(triggeredByHabitId = habitId)
        }
        entryDao.update(updated)
    }

    /**
     * Retrieves an [Entry] by its ID, including its associated [Influencer] and [HumanNeed] objects.
     *
     * @param id The ID of the [Entry] to retrieve.
     * @return The [Entry] object with its associated data.
     */
    override suspend fun getById(id: String): Entry {
        return entryDao.findEntryHabitById(id).toEntry()
            .copy(
                influencers = influencerDao.getInfluencersForEntry(id).map { it.toInfluencer() },
                humanNeeds = humanNeedDao.getHumanNeedsByEntryId(id).map { it.toHumanNeed() }
            )
    }

    /**
     * Updates an existing [Entry] in the database.
     *
     * @param entry The [Entry] object to update.
     */
    override suspend fun update(entry: Entry) {
        entryDao.update(entry.toEntity())
    }

    /**
     * Retrieves a list of [Entry] objects within a specified datetime range.
     *
     * @param start The start datetime of the range.
     * @param end The end datetime of the range.
     * @return A list of [Entry] objects within the specified range.
     */
    override suspend fun getByDatetimeRange(
        start: OffsetDateTime,
        end: OffsetDateTime
    ): List<Entry> = entryDao.findEntriesByDatetimeRange(start, end).map { it.toEntry() }
}