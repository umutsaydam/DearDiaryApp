package com.umutsaydam.deardiary.domain.useCase.auth

import com.umutsaydam.deardiary.domain.Resource
import com.umutsaydam.deardiary.domain.entity.TokenEntity
import com.umutsaydam.deardiary.domain.entity.UserEntity
import com.umutsaydam.deardiary.domain.repository.UserRepository
import javax.inject.Inject

class UserLoginUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(userEntity: UserEntity): Resource<TokenEntity> {
        return userRepository.userLogin(userEntity)
    }
}