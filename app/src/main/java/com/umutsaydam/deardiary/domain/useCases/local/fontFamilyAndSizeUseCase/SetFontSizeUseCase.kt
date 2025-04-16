package com.umutsaydam.deardiary.domain.useCases.local.fontFamilyAndSizeUseCase

import com.umutsaydam.deardiary.domain.manager.UserPreferencesManager
import javax.inject.Inject

class SetFontSizeUseCase @Inject constructor(
    private val userPreferencesManager: UserPreferencesManager
) {
    suspend operator fun invoke(fontSizeId: Int) {
        userPreferencesManager.setDefaultFontSize(fontSizeId)
    }
}