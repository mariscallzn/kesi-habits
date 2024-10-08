package com.kesicollection.data.habit.di

import com.kesicollection.data.habit.HabitRepository
import com.kesicollection.data.habit.HabitRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class HabitDataModule {

    @Binds
    internal abstract fun bindsHabitRepository(
        habitRepositoryImpl: HabitRepositoryImpl
    ): HabitRepository
}