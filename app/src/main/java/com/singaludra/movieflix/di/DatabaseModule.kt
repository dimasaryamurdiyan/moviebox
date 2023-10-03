package com.singaludra.movieflix.di

import android.content.Context
import androidx.room.Room
import com.singaludra.data.local.room.MainDatabase
import com.singaludra.data.local.room.dao.MovieDao
import com.singaludra.movieflix.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): MainDatabase = Room.databaseBuilder(
        context,
        MainDatabase::class.java, Constants.DATABASE_NAME
    ).fallbackToDestructiveMigration().build()

    @Provides
    fun provideDao(database: MainDatabase): MovieDao = database.movieDao()
}