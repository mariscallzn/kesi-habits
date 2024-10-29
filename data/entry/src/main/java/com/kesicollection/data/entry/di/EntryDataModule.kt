package com.kesicollection.data.entry.di

import com.kesicollection.data.entry.EntryRepository
import com.kesicollection.data.entry.EntryRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class EntryDataModule {
    @Binds
    internal abstract fun bindsHabitRepository(
        entryRepositoryImpl: EntryRepositoryImpl
    ): EntryRepository
}