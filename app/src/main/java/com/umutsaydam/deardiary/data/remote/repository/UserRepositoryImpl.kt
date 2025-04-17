package com.umutsaydam.deardiary.data.remote.repository

import com.umutsaydam.deardiary.data.remote.DearDiaryApiService
import com.umutsaydam.deardiary.data.remote.mapper.TokenMapper.toEntity
import com.umutsaydam.deardiary.data.remote.mapper.UserMapper.toDto
import com.umutsaydam.deardiary.domain.Resource
import com.umutsaydam.deardiary.domain.entity.TokenEntity
import com.umutsaydam.deardiary.domain.entity.UserEntity
import com.umutsaydam.deardiary.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val dearDiaryApiService: DearDiaryApiService
) : UserRepository {
    override suspend fun userCreate(userEntity: UserEntity): Resource<Unit> {
        val response = dearDiaryApiService.userCreate(userEntity.toDto())
        // 400 Username already taken.
        // 201 NO_CONTENT

        if (response.code() == 201) {
            return Resource.Success()
        } else if (response.code() == 400) {
            return Resource.Error(401, "This username already taken.")
        }
        return Resource.Error()
    }

    override suspend fun userLogin(userEntity: UserEntity): Resource<TokenEntity> {
        val response = dearDiaryApiService.userLogin(userEntity.toDto())
        // 500 Username or password wrong.
        // 200 token: TOKEN_CONTENT

        if (response.code() == 200) {
            response.body()?.let {
                val tokenEntity = it.toEntity()
                return Resource.Success(tokenEntity)
            }
        } else if (response.code() == 500) {
            return Resource.Error(500, "Username or password wrong.")
        }
        return Resource.Error()
    }

    override suspend fun userLogout(): Resource<Unit> {
        val response = dearDiaryApiService.userLogout()
        // 401 Token is wrong or expire.
        // 200 Ok

        if (response.code() == 200) {
            return Resource.Success()
        }
        return Resource.Error()
    }

    override suspend fun tokenReusable(): Resource<Boolean> {
        val response = dearDiaryApiService.tokenReusable()
        // 401 Token is wrong or expired.
        // 200 Token is still available.

        if (response.code() == 200) {
            return Resource.Success(true)
        } else if (response.code() == 401) {
            return Resource.Error(401, "You need to resign in.")
        }
        return Resource.Error()
    }
}