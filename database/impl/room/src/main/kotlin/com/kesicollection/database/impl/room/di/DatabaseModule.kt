package com.kesicollection.database.impl.room.di

import android.content.Context
import androidx.room.Room
import com.kesicollection.database.impl.room.KhDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {
    @Provides
    @Singleton
    fun providesKhDatabase(
        @ApplicationContext context: Context
    ): KhDatabase = Room.databaseBuilder(
        context, KhDatabase::class.java, "kh-database"
    ).build()
}