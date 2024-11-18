package com.kesicollection.data.entry

import com.kesicollection.core.model.Day
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
import com.kesicollection.domain.datetime.GetOffsetDateTimeFromIsoFormat
import com.kesicollection.domain.datetime.GetStartEndFromOffsetDateTime
import com.kesicollection.domain.datetime.UpdateOffsetDateTimeWithMillis
import com.kesicollection.domain.datetime.UpdateOffsetDateTimeWithTimePicker
import javax.inject.Inject

interface EntryRepository {
    /**
     * Adds or updates a list of entries, handling potential duplicates.
     *
     * @param entries The list of entries to be added or updated.
     */
    suspend fun addOrUpdateEntry(entries: List<Entry>)

    /**
     * Adds a single entry to the database.
     *
     * @param entry The entry to be added.
     * @return The ID of the added entry.
     */
    suspend fun add(entry: Entry): Long

    /**
     * Updates the habit information associated with an entry, including the habit ID, type, and a
     * list of influencer IDs.
     *
     * @param entryId The ID of the entry to be updated.
     * @param habitId The new habit ID to be associated with the entry.
     * @param habitType The type of habit being associated (e.g., habit or triggered habit).
     * @param influencersIds The list of influencer IDs associated with the entry.
     */
    suspend fun updateHabit(
        entryId: String,
        habitId: String?,
        habitType: HabitType?,
        influencersIds: List<String>?
    )

    /**
     * Retrieves an entry by its unique identifier.
     *
     * @param id The ID of the entry to retrieve.
     * @return The entry with the specified ID, or null if not found.
     */
    suspend fun getById(id: String): Entry?

    /**
     * Retrieves a list of entries for a specific day.
     *
     * @param day The day for which to retrieve entries.
     * @return A list of entries associated with the specified day.
     */
    suspend fun getByDay(day: Day): List<Entry>

    /**
     * Updates the status of an entry.
     *
     * @param id The ID of the entry to update.
     * @param status The new status for the entry.
     * @return The updated entry, or null if not found.
     */
    suspend fun updateEntryStatus(id: String, status: Status): Entry?

    /**
     * Updates the date of an entry.
     *
     * @param id The ID of the entry to update.
     * @param millis The new timestamp for the entry in milliseconds.
     */
    suspend fun updateDate(id: String, millis: Long?)

    /**
     * Updates the time of an entry.
     *
     * @param id The ID of the entry to update.
     * @param hour The new hour for the entry.
     * @param minute The new minute for the entry.
     */
    suspend fun updateTime(
        id: String, hour: Int,
        minute: Int
    )

    /**
     * Updates the list of human needs associated with an entry.
     *
     * @param id The ID of the entry to update.
     * @param humanNeeds The new list of human needs to associate with the entry.
     */
    suspend fun updateHumanNeeds(id: String, humanNeeds: List<HumanNeed>)

}

/**
 * Implementation of the [EntryRepository] interface, handling operations related to entries, their
 * associated habits, influencers, and human needs.

 * This class leverages dependency injection to access necessary data access objects and helper
 * functions for date/time manipulation and database interactions.
 *
 * @param entryDb Database access object for [Entry] entities.
 * @param entryInfluencerDb Database access object for [EntryInfluencer] entities.
 * @param entryHumanNeedDb Database access object for [EntryHumanNeed] entities.
 * @param humanNeedDb Database access object for [HumanNeed] entities.
 * @param updateOffsetDateTimeWithMillis Helper function to update an `OffsetDateTime` with milliseconds.
 * @param updateOffsetDateTimeWithTimePicker Helper function to update an `OffsetDateTime` with hour and minute.
 * @param getStartEndFromOffsetDateTime Helper function to get start and end date-times from an `OffsetDateTime`.
 * @param getOffsetDateTimeFromIsoFormat Helper function to parse an ISO 8601 formatted string into an `OffsetDateTime`.
 */
internal class EntryRepositoryImpl @Inject constructor(
    private val entryDb: EntryDb,
    private val entryInfluencerDb: EntryInfluencerDb,
    private val entryHumanNeedDb: EntryHumanNeedDb,
    private val humanNeedDb: HumanNeedDb,
    private val updateOffsetDateTimeWithMillis: UpdateOffsetDateTimeWithMillis,
    private val updateOffsetDateTimeWithTimePicker: UpdateOffsetDateTimeWithTimePicker,
    private val getStartEndFromOffsetDateTime: GetStartEndFromOffsetDateTime,
    private val getOffsetDateTimeFromIsoFormat: GetOffsetDateTimeFromIsoFormat,
) : EntryRepository {
    /**
     * Adds or updates a list of entries.

     * This function delegates the operation to the underlying `entryDb` to handle both insertion and updates as needed.

     * @param entries The list of entries to be added or updated.

     * @return The number of rows affected by the operation.
     */
    override suspend fun addOrUpdateEntry(entries: List<Entry>) = entryDb.upsertEntries(entries)

    /**
     * Adds a single entry to the database.

     * This function delegates the insertion operation to the underlying `entryDb`.

     * @param entry The entry to be added.

     * @return The ID of the newly inserted entry.
     */
    override suspend fun add(entry: Entry): Long = entryDb.insert(entry)

    /**
     * Updates the habit information associated with an entry.
     *
     * This function allows updating either the habit ID and type, or the associated influencer IDs for an entry.
     * It cannot update both simultaneously.
     *
     * @param entryId The ID of the entry to update.
     * @param habitId The new habit ID to be associated with the entry (optional).
     * @param habitType The type of habit being associated (e.g., habit or triggered habit) (optional).
     * @param influencersIds The list of influencer IDs to be associated with the entry (optional).
     * Supplying this will update the existing influencers for the entry.
     *
     * **Note:** Only one of `habitId` and `habitType` or `influencersIds` can be provided at a time.
     * Supplying both will result in undefined behavior.
     *
     * Throws an exception if both `habitId/habitType` and `influencersIds` are provided.
     */
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

    /**
     * Retrieves an entry by its ID.

     * This function retrieves an entry by its ID and then populates its associated human needs from
     * the database.

     * @param id The ID of the entry to retrieve.

     * @return The entry with the specified ID, including its associated human needs, or `null` if
     * not found.
     */
    override suspend fun getById(id: String): Entry? =
        entryDb.getById(id)?.copy(humanNeeds = entryHumanNeedDb.getEntryHumanNeedByEntryId(id).map {
            humanNeedDb.findById(it.humanNeedId).copy(ranked = it.rank)
        })

    /**
     * Updates the status of an entry.

     * This function retrieves the entry by ID, updates its status, and then persists the updated
     * entry to the database.

     * @param id The ID of the entry to update.
     * @param status The new status to be assigned to the entry.

     * @return The updated entry, or `null` if the entry with the specified ID is not found.
     */
    override suspend fun updateEntryStatus(id: String, status: Status): Entry? {
        return getById(id)?.copy(status = status)?.let { entry ->
            entryDb.update(entry)
            entry
        }
    }

    /**
     * Updates the date of an entry.

     * This function updates the `recordedOn` field of the specified entry with a new timestamp in
     * milliseconds.

     * @param id The ID of the entry to update.
     * @param millis The new timestamp in milliseconds.
     */
    override suspend fun updateDate(id: String, millis: Long?) {
        millis?.let {
            getById(id)?.let { entry ->
                val updatedDate = updateOffsetDateTimeWithMillis(entry.recordedOn, millis)
                entryDb.update(entry.copy(recordedOn = updatedDate))
            }
        }
    }

    /**
     * Updates the time of an entry.

     * This function updates the time portion of the `recordedOn` field of the specified entry while
     * preserving the date information.

     * @param id The ID of the entry to update.
     * @param hour The new hour for the entry.
     * @param minute The new minute for the entry.
     */
    override suspend fun updateTime(id: String, hour: Int, minute: Int) {
        getById(id)?.let { entry ->
            val updatedTime =
                updateOffsetDateTimeWithTimePicker(entry.recordedOn, hour, minute)
            entryDb.update(entry.copy(recordedOn = updatedTime))
        }
    }

    /**
     * Updates the list of human needs associated with an entry.

     * This function updates the human needs associated with an entry by performing an upsert operation.
     * It identifies existing entries and assigns them IDs if necessary, then performs an upsert in
     * the `entryHumanNeedDb`.

     * @param id The ID of the entry to update.
     * @param humanNeeds The new list of human needs to associate with the entry.

     * **Upsert:** An operation that combines updating existing entries and inserting new entries in
     * one call.
     */
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

    /**
     * Retrieves a list of entries for a specific day.

     * This function retrieves entries within a date range corresponding to the given `Day` object.
     * It uses helper functions to parse the `rawUTCDateTime` into an `OffsetDateTime` and then
     * extracts the start and end of the day.

     * @param day The `Day` object representing the day for which to retrieve entries.
     * @return A list of entries associated with the specified day.
     */
    override suspend fun getByDay(day: Day): List<Entry> =
        getStartEndFromOffsetDateTime(getOffsetDateTimeFromIsoFormat(day.rawUTCDateTime)).let {
            entryDb.getByDatetimeRange(it.first, it.second)
        }
}