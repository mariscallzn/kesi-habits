package com.kesicollection.database.impl.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kesicollection.database.impl.room.dao.EmotionDao
import com.kesicollection.database.impl.room.dao.EntryDao
import com.kesicollection.database.impl.room.dao.EntryEmotionDao
import com.kesicollection.database.impl.room.dao.EntryInfluencerDao
import com.kesicollection.database.impl.room.dao.HabitDao
import com.kesicollection.database.impl.room.dao.InfluencerDao
import com.kesicollection.database.impl.room.model.EmotionEntity
import com.kesicollection.database.impl.room.model.EntryEmotionCrossRef
import com.kesicollection.database.impl.room.model.EntryEntity
import com.kesicollection.database.impl.room.model.EntryInfluencerCrossRef
import com.kesicollection.database.impl.room.model.HabitEntity
import com.kesicollection.database.impl.room.model.InfluencerEntity
import com.kesicollection.database.impl.room.util.ArousalConverter
import com.kesicollection.database.impl.room.util.ClassificationConverter
import com.kesicollection.database.impl.room.util.EmotionTypeConverter
import com.kesicollection.database.impl.room.util.OffsetDateTimeConverter
import com.kesicollection.database.impl.room.util.StatusConverter
import com.kesicollection.database.impl.room.util.ValenceConverter

@Database(
    entities = [
        HabitEntity::class,
        EntryEntity::class,
        EmotionEntity::class,
        InfluencerEntity::class,
        EntryInfluencerCrossRef::class,
        EntryEmotionCrossRef::class,
    ], version = 1
)
@TypeConverters(
    OffsetDateTimeConverter::class,
    ClassificationConverter::class,
    ValenceConverter::class,
    ArousalConverter::class,
    StatusConverter::class,
    EmotionTypeConverter::class
)
internal abstract class KhDatabase : RoomDatabase() {
    abstract fun habitDao(): HabitDao
    abstract fun entryDao(): EntryDao
    abstract fun influencerDao(): InfluencerDao
    abstract fun emotionDao(): EmotionDao
    abstract fun entryInfluencerDao(): EntryInfluencerDao
    abstract fun entryEmotionDao(): EntryEmotionDao
}