package com.kesicollection.data.emotion.di

import com.kesicollection.data.emotion.EmotionRepository
import com.kesicollection.data.emotion.EmotionRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class EmotionDataModule {
    @Binds
    internal abstract fun bindsEmotionRepository(
        emotionRepositoryImpl: EmotionRepositoryImpl
    ): EmotionRepository
}