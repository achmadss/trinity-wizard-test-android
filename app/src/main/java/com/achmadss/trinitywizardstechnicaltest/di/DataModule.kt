package com.achmadss.trinitywizardstechnicaltest.di

import android.content.Context
import androidx.room.Room
import com.achmadss.data.local.room.AppDataDao
import com.achmadss.data.local.room.AppDatabase
import com.achmadss.data.repository.AppDataRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    fun roomDatabase(
        @ApplicationContext context: Context
    ): AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        AppDatabase.DATABASE_NAME
    ).fallbackToDestructiveMigration().build()

    @Provides
    fun provideDataDao(appDb: AppDatabase): AppDataDao = appDb.dataDao()

    @Provides
    fun provideDataRepository(
        appDataDao: AppDataDao
    ) : AppDataRepository = AppDataRepository(appDataDao)

}