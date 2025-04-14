package com.umutsaydam.deardiary.di

import android.content.Context
import com.umutsaydam.deardiary.data.local.TokenManagerImpl
import com.umutsaydam.deardiary.data.remote.DearDiaryApiService
import com.umutsaydam.deardiary.data.remote.repository.UserRepositoryImpl
import com.umutsaydam.deardiary.domain.manager.TokenManager
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
    fun provideUserRepository(
        dearDiaryApiService: DearDiaryApiService
    ): UserRepository = UserRepositoryImpl(dearDiaryApiService)

    @Provides
    @Singleton
    fun provideTokenManager(
        @ApplicationContext context: Context
    ): TokenManager = TokenManagerImpl(context)
}