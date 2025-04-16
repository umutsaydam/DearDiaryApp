package com.umutsaydam.deardiary.domain.useCases.local.fontFamilyAndSizeUseCase

import com.umutsaydam.deardiary.domain.manager.UserPreferencesManager
import javax.inject.Inject

class SetFontFamilyUseCase @Inject constructor(
    private val userPreferencesManager: UserPreferencesManager
) {
    suspend operator fun invoke(fontFamilyId: Int){
        userPreferencesManager.setDefaultFontFamily(fontFamilyId)
    }
}