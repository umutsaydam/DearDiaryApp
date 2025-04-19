package com.umutsaydam.deardiary.domain.repository

import com.umutsaydam.deardiary.domain.sealedStates.Resource
import com.umutsaydam.deardiary.domain.entity.TokenEntity
import com.umutsaydam.deardiary.domain.entity.UserEntity

interface UserRepository {
    suspend fun userCreate(userEntity: UserEntity): Resource<Unit>

    suspend fun userLogin(userEntity: UserEntity): Resource<TokenEntity>

    suspend fun userLogout(): Resource<Unit>

    suspend fun tokenReusable(): Resource<Boolean>
}