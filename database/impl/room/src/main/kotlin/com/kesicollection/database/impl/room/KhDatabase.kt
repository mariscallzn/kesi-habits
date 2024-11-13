package com.kesicollection.database.impl.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kesicollection.database.impl.room.dao.EntryDao
import com.kesicollection.database.impl.room.dao.EntryHumanNeedDao
import com.kesicollection.database.impl.room.dao.EntryInfluencerDao
import com.kesicollection.database.impl.room.dao.HabitDao
import com.kesicollection.database.impl.room.dao.HumanNeedDao
import com.kesicollection.database.impl.room.dao.InfluencerDao
import com.kesicollection.database.impl.room.model.EntryEntity
import com.kesicollection.database.impl.room.model.EntryHumanNeedCrossRef
import com.kesicollection.database.impl.room.model.EntryInfluencerCrossRef
import com.kesicollection.database.impl.room.model.HabitEntity
import com.kesicollection.database.impl.room.model.HumanNeedEntity
import com.kesicollection.database.impl.room.model.InfluencerEntity
import com.kesicollection.database.impl.room.util.ArousalConverter
import com.kesicollection.database.impl.room.util.ClassificationConverter
import com.kesicollection.database.impl.room.util.OffsetDateTimeConverter
import com.kesicollection.database.impl.room.util.StatusConverter
import com.kesicollection.database.impl.room.util.ValenceConverter

@Database(
    entities = [
        HabitEntity::class,
        EntryEntity::class,
        InfluencerEntity::class,
        EntryInfluencerCrossRef::class,
        HumanNeedEntity::class,
        EntryHumanNeedCrossRef::class
    ], version = 1
)
@TypeConverters(
    OffsetDateTimeConverter::class,
    ClassificationConverter::class,
    ValenceConverter::class,
    ArousalConverter::class,
    StatusConverter::class,
)
internal abstract class KhDatabase : RoomDatabase() {
    abstract fun habitDao(): HabitDao
    abstract fun entryDao(): EntryDao
    abstract fun influencerDao(): InfluencerDao
    abstract fun entryInfluencerDao(): EntryInfluencerDao
    abstract fun humanNeedDao(): HumanNeedDao
    abstract fun entryHumanNeedDao(): EntryHumanNeedDao
}