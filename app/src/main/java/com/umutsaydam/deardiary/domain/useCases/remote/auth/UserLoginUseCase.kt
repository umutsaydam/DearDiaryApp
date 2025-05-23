package com.umutsaydam.deardiary.domain.useCases.remote.auth

import com.umutsaydam.deardiary.domain.sealedStates.Resource
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