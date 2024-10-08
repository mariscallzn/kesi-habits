package com.kesicollection.database.impl.room.di

import com.kesicollection.database.impl.room.KhDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal object DaosModule {
    @Provides
    fun providesHabitDao(database: KhDatabase) = database.habitDao()
}