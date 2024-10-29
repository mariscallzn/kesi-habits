package com.kesicollection.kesihabits.di

import com.kesicollection.database.api.EntryDb
import com.kesicollection.database.api.HabitDb
import com.kesicollection.database.impl.room.api.RoomEntryDb
import com.kesicollection.database.impl.room.api.RoomHabitDb
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

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
}