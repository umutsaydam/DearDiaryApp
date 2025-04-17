package com.umutsaydam.deardiary.domain.useCases.local.securityUseCase.fingerPrint

import com.umutsaydam.deardiary.domain.manager.UserPreferencesManager
import javax.inject.Inject

class SetFingerPrintEnabledUseCase @Inject constructor(
    private val userPreferencesManager: UserPreferencesManager
) {
    suspend operator fun invoke(enabled: Boolean) {
        userPreferencesManager.setFingerPrintEnable(enabled)
    }
}