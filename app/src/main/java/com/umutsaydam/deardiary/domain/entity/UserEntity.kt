package com.umutsaydam.deardiary.domain.entity

import java.util.UUID

data class UserEntity(
    val userId: UUID? = null,
    val username: String,
    val password: String
)
