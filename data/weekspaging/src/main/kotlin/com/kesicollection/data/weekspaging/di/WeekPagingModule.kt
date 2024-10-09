package com.kesicollection.data.weekspaging.di

import com.kesicollection.data.weekspaging.WeekPageConfig
import com.kesicollection.data.weekspaging.WeekPageConfigImpl
import com.kesicollection.data.weekspaging.WeekPagingSourceImpl
import com.kesicollection.data.weekspaging.WeekPagingSource
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class WeekPagingImplementationModule {

    @Binds
    abstract fun bindWeekPagingSource(
        weekPagingImpl: WeekPagingSourceImpl
    ): WeekPagingSource
}


@Module
@InstallIn(SingletonComponent::class)
internal object WeekPagingModule {

    @Provides
    fun providesWeekPageConfig(): WeekPageConfig = WeekPageConfigImpl(10, 2)
}