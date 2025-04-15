package com.umutsaydam.deardiary.domain.useCases.remote.auth

import com.umutsaydam.deardiary.domain.Resource
import com.umutsaydam.deardiary.domain.repository.UserRepository
import javax.inject.Inject

class TokenReusable @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Resource<Boolean> {
        return userRepository.tokenReusable()
    }
}