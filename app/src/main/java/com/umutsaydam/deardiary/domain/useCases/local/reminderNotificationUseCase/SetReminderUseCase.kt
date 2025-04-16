package com.umutsaydam.deardiary.domain.useCases.local.reminderNotificationUseCase

import com.umutsaydam.deardiary.domain.manager.UserPreferencesManager
import javax.inject.Inject

class SetReminderUseCase @Inject constructor(
    private val userPreferencesManager: UserPreferencesManager
) {
    suspend operator fun invoke(enabled: Boolean){
        userPreferencesManager.setReminderEnabled(enabled)
    }
}