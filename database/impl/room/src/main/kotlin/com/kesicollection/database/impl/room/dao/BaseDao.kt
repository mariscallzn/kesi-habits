package com.kesicollection.database.impl.room.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import androidx.room.Upsert

//https://medium.com/androiddevelopers/room-time-2b4cf9672b98
interface BaseDao<T> {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertAll(items: List<T>): List<Long>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(item: T): Long

    /**
     * An @Update method can optionally return an int value indicating the number of rows that
     * were updated successfully.
     */
    @Update(onConflict = OnConflictStrategy.ABORT)
    suspend fun updateAll(items: List<T>): Int

    @Update(onConflict = OnConflictStrategy.ABORT)
    suspend fun update(item: T)

    /**
     * A @Delete method can optionally return an int value indicating the number of rows that
     * were deleted successfully.
     */
    @Delete
    suspend fun deleteAll(items: List<T>): Int

    @Delete
    suspend fun delete(item: T)

    /**
     * Inserts or updates [T] in the db under the specified primary key
     */
    @Upsert
    suspend fun upsertAll(items: List<T>)

    @Upsert
    suspend fun upsert(item: T)
}