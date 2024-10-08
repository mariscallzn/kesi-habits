package com.kesicollection.database.impl.room.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

//https://medium.com/androiddevelopers/room-time-2b4cf9672b98
interface BaseDao<T> {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertAll(vararg items: T): List<Long>

    /**
     * An @Update method can optionally return an int value indicating the number of rows that
     * were updated successfully.
     */
    @Update(onConflict = OnConflictStrategy.ABORT)
    suspend fun updateAll(vararg items: T): Int

    /**
     * A @Delete method can optionally return an int value indicating the number of rows that
     * were deleted successfully.
     */
    @Delete
    suspend fun deleteAll(vararg items: T): Int
}