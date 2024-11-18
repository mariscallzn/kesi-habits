package com.kesicollection.database.api

import com.kesicollection.core.model.Entry
import com.kesicollection.core.model.HabitType
import java.time.OffsetDateTime

interface EntryDb {
    /**
     * Inserts or updates a list of entries into the database.
     * @param entries The list of entries to be upserted.
     */
    suspend fun upsertEntries(entries: List<Entry>)

    /**
     * Inserts a single entry into the database.
     *
     * @param entry The entry to be inserted.
     * @return The ID of the inserted entry.
     */
    suspend fun insert(entry: Entry): Long

    /**
     * Updates the habit information associated with an entry.
     *
     * @param entryId The ID of the entry to be updated.
     * @param habitId The new habit ID to be associated with the entry.
     * @param type The type of habit being associated (e.g., core habit or triggered habit).
     */
    suspend fun updateHabit(entryId: String, habitId: String?, type: HabitType)

    /**
     * Retrieves an entry by its unique identifier.
     *
     * @param id The ID of the entry to retrieve.
     * @return The entry with the specified ID, or null if not found.
     */
    suspend fun getById(id: String): Entry?

    /**
     * Updates an existing entry in the database.
     *
     * @param entry The updated entry to be persisted.
     */
    suspend fun update(entry: Entry)

    /**
     * Retrieves a list of entries within a specific date-time range.
     *
     * @param start The starting date-time of the desired range (inclusive).
     * @param end The ending date-time of the desired range (inclusive).
     * @return A list of entries within the specified date-time range.
     */
    suspend fun getByDatetimeRange(start: OffsetDateTime, end: OffsetDateTime): List<Entry>
}