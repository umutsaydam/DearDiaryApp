package com.umutsaydam.deardiary.domain.useCases.local.securityUseCase.pin

import com.umutsaydam.deardiary.domain.manager.UserPreferencesManager
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPinUseCase @Inject constructor(
    private val userPreferencesManager: UserPreferencesManager
) {
    operator fun invoke(): Flow<String> {
        return userPreferencesManager.getPin()
    }
}