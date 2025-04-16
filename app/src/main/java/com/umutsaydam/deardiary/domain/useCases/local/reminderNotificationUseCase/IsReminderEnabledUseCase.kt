package com.umutsaydam.deardiary.domain.useCases.local.reminderNotificationUseCase

import com.umutsaydam.deardiary.domain.manager.UserPreferencesManager
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class IsReminderEnabledUseCase @Inject constructor(
    private val userPreferencesManager: UserPreferencesManager
) {
    operator fun invoke(): Flow<Boolean> {
        return userPreferencesManager.isReminderEnabled()
    }
}