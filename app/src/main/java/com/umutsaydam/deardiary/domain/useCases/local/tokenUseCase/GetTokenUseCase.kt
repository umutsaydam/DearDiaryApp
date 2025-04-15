package com.umutsaydam.deardiary.domain.useCases.local.tokenUseCase

import com.umutsaydam.deardiary.domain.manager.TokenManager
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTokenUseCase @Inject constructor(
    private val tokenManager: TokenManager
) {
    suspend operator fun invoke(): Flow<String?>{
        return tokenManager.getTokenFlow()
    }
}