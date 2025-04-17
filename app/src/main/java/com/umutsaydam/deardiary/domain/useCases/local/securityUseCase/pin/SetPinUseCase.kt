package com.umutsaydam.deardiary.domain.useCases.local.securityUseCase.pin

import com.umutsaydam.deardiary.domain.manager.UserPreferencesManager
import javax.inject.Inject

class SetPinUseCase @Inject constructor(
    private val userPreferencesManager: UserPreferencesManager
) {
    suspend operator fun invoke(pin: String) {
        userPreferencesManager.setPin(pin)
    }
}