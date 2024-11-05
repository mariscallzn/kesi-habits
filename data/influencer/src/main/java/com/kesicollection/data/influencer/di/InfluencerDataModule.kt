package com.kesicollection.data.influencer.di

import com.kesicollection.data.influencer.InfluencerRepository
import com.kesicollection.data.influencer.InfluencerRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class InfluencerDataModule {

    @Binds
    internal abstract fun bindsInfluencerRepository(
        influencerRepositoryImpl: InfluencerRepositoryImpl
    ): InfluencerRepository
}