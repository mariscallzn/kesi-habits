package com.kesicollection.database.impl.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kesicollection.database.impl.room.dao.EmotionDao
import com.kesicollection.database.impl.room.dao.EntryCurrentEmotionDao
import com.kesicollection.database.impl.room.dao.EntryDao
import com.kesicollection.database.impl.room.dao.EntryDesireEmotionDao
import com.kesicollection.database.impl.room.dao.EntryInfluencerDao
import com.kesicollection.database.impl.room.dao.HabitDao
import com.kesicollection.database.impl.room.dao.InfluencerDao
import com.kesicollection.database.impl.room.model.EmotionEntity
import com.kesicollection.database.impl.room.model.EntryCurrentEmotionCrossRef
import com.kesicollection.database.impl.room.model.EntryDesireEmotionCrossRef
import com.kesicollection.database.impl.room.model.EntryEntity
import com.kesicollection.database.impl.room.model.EntryInfluencerCrossRef
import com.kesicollection.database.impl.room.model.HabitEntity
import com.kesicollection.database.impl.room.model.InfluencerEntity
import com.kesicollection.database.impl.room.util.ArousalConverter
import com.kesicollection.database.impl.room.util.ClassificationConverter
import com.kesicollection.database.impl.room.util.OffsetDateTimeConverter
import com.kesicollection.database.impl.room.util.StatusConverter
import com.kesicollection.database.impl.room.util.ValenceConverter

/**
 * Database definition
 *
 * Habits
 * - Name (Drink water)
 * - Classification (Positive, negative, neutral)
 *
 * Entries
 * - Habit
 * - Trigger by (Other Habit)
 * - recordedOn
 * - Influence by (Thought, person, desire, environment, work, hobby)
 * - moodAtEntry (optional; desperate, anxious, happy, productive, energetic)
 * - desireFeelingByEntry (optional; pleasure, peace, excitement, certainty, uncertainty)
 *
 * Influencers
 * - Name
 * - i18Key (key to find the string resource for internalization)
 *
 * EmotionEntity(
 *     @PrimaryKey
 *     val id: String,
 *     val name: String,
 *     val valence: Valence,
 *     val arousal: Arousal,
 *     val i18Key: String?
 * )
 */
@Database(
    entities = [
        HabitEntity::class,
        EntryEntity::class,
        EmotionEntity::class,
        InfluencerEntity::class,
        EntryInfluencerCrossRef::class,
        EntryCurrentEmotionCrossRef::class,
        EntryDesireEmotionCrossRef::class,
    ], version = 1
)
@TypeConverters(
    OffsetDateTimeConverter::class,
    ClassificationConverter::class,
    ValenceConverter::class,
    ArousalConverter::class,
    StatusConverter::class
)
internal abstract class KhDatabase : RoomDatabase() {
    abstract fun habitDao(): HabitDao
    abstract fun entryDao(): EntryDao
    abstract fun influencerDao(): InfluencerDao
    abstract fun emotionDao(): EmotionDao
    abstract fun entryInfluencerDao(): EntryInfluencerDao
    abstract fun entryCurrentEmotionDao(): EntryCurrentEmotionDao
    abstract fun entryDesireEmotionDao(): EntryDesireEmotionDao
}