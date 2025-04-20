package com.umutsaydam.deardiary.data.remote.repository

import com.umutsaydam.deardiary.R
import com.umutsaydam.deardiary.data.remote.DearDiaryApiService
import com.umutsaydam.deardiary.data.remote.mapper.TokenMapper.toEntity
import com.umutsaydam.deardiary.data.remote.mapper.UserMapper.toDto
import com.umutsaydam.deardiary.data.remote.safeApiCall
import com.umutsaydam.deardiary.domain.sealedStates.Resource
import com.umutsaydam.deardiary.domain.entity.TokenEntity
import com.umutsaydam.deardiary.domain.entity.UserEntity
import com.umutsaydam.deardiary.domain.repository.UserRepository
import com.umutsaydam.deardiary.domain.useCases.IsInternetAvailableUseCase
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val dearDiaryApiService: DearDiaryApiService,
    private val isInternetAvailableUseCase: IsInternetAvailableUseCase
) : UserRepository {
    override suspend fun userCreate(userEntity: UserEntity): Resource<Unit> {
        // 400 Username already taken.
        // 201 NO_CONTENT
        return safeApiCall(
            isInternetAvailable = isInternetAvailableUseCase(),
            apiCall = { dearDiaryApiService.userCreate(userEntity.toDto()) },
            successCode = 201,
            errorMessages = mapOf(
                400 to R.string.username_taken
            ),
            map = {}
        )
    }

    override suspend fun userLogin(userEntity: UserEntity): Resource<TokenEntity> {
        // 500 Username or password wrong.
        // 200 token: TOKEN_CONTENT
        return safeApiCall(
            isInternetAvailable = isInternetAvailableUseCase(),
            apiCall = { dearDiaryApiService.userLogin(userEntity.toDto()) },
            successCode = 200,
            errorMessages = mapOf(
                500 to R.string.username_password_wrong
            ),
            map = { loginUserDto ->
                loginUserDto.toEntity()
            }
        )
    }

    override suspend fun userLogout(): Resource<Unit> {
        // 401 Token is wrong or expire.
        // 200 Ok
        return safeApiCall(
            isInternetAvailable = isInternetAvailableUseCase(),
            apiCall = { dearDiaryApiService.userLogout() },
            successCode = 200,
            errorMessages = mapOf(
                401 to R.string.need_resign
            ),
            map = {}
        )
    }

    override suspend fun tokenReusable(): Resource<Boolean> {
        // 401 Token is wrong or expired.
        // 200 Token is still available.
        return safeApiCall(
            isInternetAvailable = isInternetAvailableUseCase(),
            apiCall = { dearDiaryApiService.tokenReusable() },
            successCode = 200,
            errorMessages = mapOf(
                401 to R.string.need_resign
            ),
            map = {
                true
            }
        )
    }
}