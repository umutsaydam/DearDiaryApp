package com.umutsaydam.deardiary.domain.useCases.local.securityUseCase.fingerPrint

import com.umutsaydam.deardiary.domain.manager.UserPreferencesManager
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class IsFingerPrintEnabledUseCase @Inject constructor(
    private val userPreferencesManager: UserPreferencesManager
) {
    operator fun invoke(): Flow<Boolean> {
        return userPreferencesManager.isFingerPrintEnabled()
    }
}