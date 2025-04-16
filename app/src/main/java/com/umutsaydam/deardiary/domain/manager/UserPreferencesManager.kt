package com.umutsaydam.deardiary.domain.manager

import kotlinx.coroutines.flow.Flow

interface UserPreferencesManager {
    suspend fun setReminderEnabled(enabled: Boolean)
    fun isReminderEnabled(): Flow<Boolean>
}