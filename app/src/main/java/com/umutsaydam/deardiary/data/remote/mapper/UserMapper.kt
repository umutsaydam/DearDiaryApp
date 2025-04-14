package com.umutsaydam.deardiary.data.remote.mapper

import com.umutsaydam.deardiary.data.remote.dto.UserDto
import com.umutsaydam.deardiary.domain.entity.UserEntity

object UserMapper {
    fun UserDto.toEntity(): UserEntity {
        return UserEntity(
            userId = this.userId,
            username = this.username,
            password = this.password
        )
    }

    fun UserEntity.toDto(): UserDto {
        return UserDto(
            userId = this.userId,
            username = this.username,
            password = this.password
        )
    }
}