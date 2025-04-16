package com.umutsaydam.deardiary.domain.manager

import kotlinx.coroutines.flow.Flow

interface UserPreferencesManager {
    suspend fun setReminderEnabled(enabled: Boolean)
    fun isReminderEnabled(): Flow<Boolean>
    suspend fun setDefaultFontFamily(fontFamilyId: Int)
    fun getDefaultFontFamily(): Flow<Int>
    suspend fun setDefaultFontSize(fontSizeId: Int)
    fun getDefaultFontSize(): Flow<Int>
}