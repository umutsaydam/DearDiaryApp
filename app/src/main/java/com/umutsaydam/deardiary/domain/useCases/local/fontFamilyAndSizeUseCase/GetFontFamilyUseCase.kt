package com.umutsaydam.deardiary.domain.useCases.local.fontFamilyAndSizeUseCase

import com.umutsaydam.deardiary.domain.manager.UserPreferencesManager
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFontFamilyUseCase @Inject constructor(
    private val userPreferencesManager: UserPreferencesManager
) {
    operator fun invoke(): Flow<Int> {
        return userPreferencesManager.getDefaultFontFamily()
    }
}