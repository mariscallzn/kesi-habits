package com.kesicollection.database.impl.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kesicollection.database.impl.room.dao.HabitDao
import com.kesicollection.database.impl.room.model.HabitEntity
import com.kesicollection.database.impl.room.util.OffsetDateTimeConverter

@Database(entities = [HabitEntity::class], version = 1)
@TypeConverters(OffsetDateTimeConverter::class)
internal abstract class KhDatabase: RoomDatabase() {
    abstract fun habitDao(): HabitDao
}