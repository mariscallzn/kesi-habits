package com.kesicollection.data.humanneed.di

import com.kesicollection.data.humanneed.HumanNeedRepository
import com.kesicollection.data.humanneed.HumanNeedRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class HumanNeedModule {

    @Binds
    internal abstract fun bindsHumanNeedRepository(
        humanNeedRepository: HumanNeedRepositoryImpl
    ): HumanNeedRepository
}