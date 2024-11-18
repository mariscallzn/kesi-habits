package com.kesicollection.database.impl.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.kesicollection.core.model.Status
import com.kesicollection.database.impl.room.model.EntryEntity
import com.kesicollection.database.impl.room.model.EntryHabitJoin
import java.time.OffsetDateTime

/**
 * Data access object (DAO) interface for interacting with [EntryEntity] objects in a database.
 *
 * This interface inherits from [BaseDao]<[EntryEntity]>, providing basic CRUD operations for [EntryEntity].
 *
 * Additionally, it defines custom queries for more specific data retrieval:
 * - `findById`: Retrieves an [EntryEntity] by its ID.
 * - `findEntryHabitById`: Retrieves an [EntryHabitJoin] object containing detailed information about an entry and its associated habit (including triggered habit details if available).
 * - `findEntriesByDatetimeRange`: Retrieves a list of [EntryHabitJoin] objects for entries within a specific date-time range and with a given status (defaults to [Status.ACTIVE]).
 */
@Dao
interface EntryDao : BaseDao<EntryEntity> {

    /**
     * Retrieves an [EntryEntity] object from the database by its ID.
     *
     * @param id The unique identifier of the entry to retrieve.
     * @return An [EntryEntity] object representing the entry with the specified ID, or null if not found.
     */
    @Query(
        """
        SELECT * FROM entries
        WHERE id = :id
    """
    )
    suspend fun findById(id: String): EntryEntity

    /**
     * Retrieves an [EntryHabitJoin] object containing detailed information about an entry and its associated habit (including triggered habit details if available).
     *
     * @param id The unique identifier of the entry to retrieve.
     * @return An [EntryHabitJoin] object containing detailed information about the entry and its related habits, or null if not found.
     */
    @Query(
        """
        SELECT e.id, e.recorded_on AS recordedOn, e.status, h.id AS habitId, h.name AS habitName, h.classification AS habitClassification, h.status AS habitStatus, t.id AS triggeredHabitId, t.name AS triggeredHabitName, t.classification AS triggeredHabitClassification, t.status AS triggeredHabitStatus  
        FROM entries e
        LEFT JOIN habits h ON e.habit_id = h.id 
        LEFT JOIN habits t ON e.triggered_by_habit_id = t.id
        WHERE e.id = :id
    """
    )
    suspend fun findEntryHabitById(id: String): EntryHabitJoin

    /**
     * Retrieves a list of [EntryHabitJoin] objects for entries within a specific date-time range and with a given status.
     *
     * @param start The starting date-time of the desired range (inclusive).
     * @param end The ending date-time of the desired range (inclusive).
     * @param status The status of the entries to retrieve (defaults to [Status.ACTIVE]).
     * @return A list of [EntryHabitJoin] objects representing entries within the specified range and with the provided status.
     */
    @Query(
        """
        SELECT e.id, e.recorded_on AS recordedOn, e.status, h.id AS habitId, h.name AS habitName, h.classification AS habitClassification, h.status AS habitStatus, t.id AS triggeredHabitId, t.name AS triggeredHabitName, t.classification AS triggeredHabitClassification, t.status AS triggeredHabitStatus  
        FROM entries e
        LEFT JOIN habits h ON e.habit_id = h.id 
        LEFT JOIN habits t ON e.triggered_by_habit_id = t.id
        WHERE recordedOn BETWEEN :start AND :end
        AND e.status = :status
    """
    )
    suspend fun findEntriesByDatetimeRange(
        start: OffsetDateTime,
        end: OffsetDateTime,
        status: Status = Status.ACTIVE
    ): List<EntryHabitJoin>
}