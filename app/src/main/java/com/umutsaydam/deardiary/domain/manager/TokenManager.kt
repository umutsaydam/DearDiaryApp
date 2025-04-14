package com.umutsaydam.deardiary.domain.manager

import kotlinx.coroutines.flow.Flow

interface TokenManager {
    suspend fun saveToken(token: String)
    fun getTokenFlow(): Flow<String?>
}
