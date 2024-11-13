package com.kesicollection.database.impl.room.di

import com.kesicollection.database.impl.room.KhDatabase
import com.kesicollection.database.impl.room.dao.EntryDao
import com.kesicollection.database.impl.room.dao.EntryHumanNeedDao
import com.kesicollection.database.impl.room.dao.EntryInfluencerDao
import com.kesicollection.database.impl.room.dao.HabitDao
import com.kesicollection.database.impl.room.dao.HumanNeedDao
import com.kesicollection.database.impl.room.dao.InfluencerDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal object DaosModule {
    @Provides
    fun providesHabitDao(database: KhDatabase): HabitDao = database.habitDao()

    @Provides
    fun providesEntryDao(database: KhDatabase): EntryDao = database.entryDao()

    @Provides
    fun providesInfluencerDao(database: KhDatabase): InfluencerDao = database.influencerDao()

    @Provides
    fun providesEntryInfluencerDao(database: KhDatabase): EntryInfluencerDao =
        database.entryInfluencerDao()

    @Provides
    fun providesHumanNeedDao(database: KhDatabase): HumanNeedDao =
        database.humanNeedDao()

    @Provides
    fun providesEntryHumanNeedDao(database: KhDatabase): EntryHumanNeedDao =
        database.entryHumanNeedDao()
}