package com.umutsaydam.deardiary.di

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.umutsaydam.deardiary.data.NetworkConnectivityObserver
import com.umutsaydam.deardiary.data.local.dataStore.TokenManagerImpl
import com.umutsaydam.deardiary.data.local.dataStore.UserPreferencesManagerImpl
import com.umutsaydam.deardiary.data.local.db.DearDiaryDB
import com.umutsaydam.deardiary.data.local.db.DiaryDao
import com.umutsaydam.deardiary.data.notification.ReminderSchedulerImpl
import com.umutsaydam.deardiary.data.remote.DearDiaryApiService
import com.umutsaydam.deardiary.data.remote.repository.DiaryRepositoryImpl
import com.umutsaydam.deardiary.data.remote.repository.InsightsRepositoryImpl
import com.umutsaydam.deardiary.data.remote.repository.UserRepositoryImpl
import com.umutsaydam.deardiary.domain.ConnectivityObserver
import com.umutsaydam.deardiary.domain.manager.TokenManager
import com.umutsaydam.deardiary.domain.manager.UserPreferencesManager
import com.umutsaydam.deardiary.domain.notification.ReminderScheduler
import com.umutsaydam.deardiary.domain.repository.DiaryRepository
import com.umutsaydam.deardiary.domain.repository.InsightsRepository
import com.umutsaydam.deardiary.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNetworkConnectivityObserver(
        application: Application
    ): ConnectivityObserver = NetworkConnectivityObserver(application)

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            produceFile = { context.preferencesDataStoreFile("secure_datastore") }
        )
    }

    @Provides
    fun provideInsightsRepository(
        dearDiaryApiService: DearDiaryApiService
    ): InsightsRepository = InsightsRepositoryImpl(dearDiaryApiService)

    @Provides
    fun provideUserRepository(
        dearDiaryApiService: DearDiaryApiService
    ): UserRepository = UserRepositoryImpl(dearDiaryApiService)

    @Provides
    fun provideDiaryRepository(
        dearDiaryApiService: DearDiaryApiService
    ): DiaryRepository = DiaryRepositoryImpl(dearDiaryApiService)

    @Provides
    @Singleton
    fun provideTokenManager(
        dataStore: DataStore<Preferences>
    ): TokenManager = TokenManagerImpl(dataStore)

    @Provides
    @Singleton
    fun provideReminderScheduler(): ReminderScheduler = ReminderSchedulerImpl()

    @Provides
    @Singleton
    fun provideUserPreferencesManager(
        dataStore: DataStore<Preferences>
    ): UserPreferencesManager = UserPreferencesManagerImpl(dataStore)

    @Provides
    @Singleton
    fun provideDearDiaryDatabase(
        application: Application
    ): DearDiaryDB {
        return Room.databaseBuilder(
            context = application.applicationContext,
            klass = DearDiaryDB::class.java,
            name = "DearDiaryDB"
        ).build()
    }

    @Provides
    @Singleton
    fun provideDiaryDao(
        dearDiaryDB: DearDiaryDB
    ): DiaryDao = dearDiaryDB.diaryDao
}