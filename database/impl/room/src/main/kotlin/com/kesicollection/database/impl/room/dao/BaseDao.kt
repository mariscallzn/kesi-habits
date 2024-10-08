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

    /**
     * An @Update method can optionally return an int value indicating the number of rows that
     * were updated successfully.
     */
    @Update(onConflict = OnConflictStrategy.ABORT)
    suspend fun updateAll(items: List<T>): Int

    /**
     * A @Delete method can optionally return an int value indicating the number of rows that
     * were deleted successfully.
     */
    @Delete
    suspend fun deleteAll(items: List<T>): Int

    /**
     * Inserts or updates [T] in the db under the specified primary key
     */
    @Upsert
    suspend fun upsertAll(items: List<T>)
}