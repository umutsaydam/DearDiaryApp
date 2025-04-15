package com.umutsaydam.deardiary.domain.useCases.local.tokenUseCase

import com.umutsaydam.deardiary.domain.manager.TokenManager
import javax.inject.Inject

class SaveTokenUseCase @Inject constructor(
    private val tokenManager: TokenManager
) {
    suspend operator fun invoke(token: String){
        tokenManager.saveToken(token)
    }
}