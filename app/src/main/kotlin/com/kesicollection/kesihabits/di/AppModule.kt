package com.kesicollection.kesihabits.di

import com.kesicollection.database.api.EntryDb
import com.kesicollection.database.api.EntryHumanNeedDb
import com.kesicollection.database.api.EntryInfluencerDb
import com.kesicollection.database.api.HabitDb
import com.kesicollection.database.api.InfluencerDb
import com.kesicollection.database.impl.room.api.RoomEntryDb
import com.kesicollection.database.impl.room.api.RoomEntryHumanNeedDb
import com.kesicollection.database.impl.room.api.RoomEntryInfluencerDb
import com.kesicollection.database.impl.room.api.RoomHabitDb
import com.kesicollection.database.impl.room.api.RoomInfluencerDb
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.time.ZoneId
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    abstract fun bindsHabitDb(
        habitDb: RoomHabitDb
    ): HabitDb

    @Binds
    abstract fun bindsEntryDb(
        entryDb: RoomEntryDb
    ): EntryDb

    @Binds
    abstract fun bindsInfluencerDb(
        influencerDb: RoomInfluencerDb
    ): InfluencerDb

    @Binds
    abstract fun bindsEntryInfluencerDb(
        entryInfluencerDb: RoomEntryInfluencerDb
    ): EntryInfluencerDb

    @Binds
    abstract fun bindsEntryHumanNeedDb(
        entryHumanNeedDb: RoomEntryHumanNeedDb
    ): EntryHumanNeedDb
}

@Module
@InstallIn(SingletonComponent::class)
object TimeZoneModule {
    @Provides
    @Singleton
    fun providesZoneId(): ZoneId = ZoneId.systemDefault()
}
