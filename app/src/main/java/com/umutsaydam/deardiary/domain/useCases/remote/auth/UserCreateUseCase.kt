package com.umutsaydam.deardiary.domain.useCases.remote.auth

import com.umutsaydam.deardiary.domain.sealedStates.Resource
import com.umutsaydam.deardiary.domain.entity.UserEntity
import com.umutsaydam.deardiary.domain.repository.UserRepository
import javax.inject.Inject

class UserCreateUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(userEntity: UserEntity): Resource<Unit> {
        return userRepository.userCreate(userEntity)
    }
}