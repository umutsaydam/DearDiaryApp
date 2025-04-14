package com.umutsaydam.deardiary.data.remote.mapper

import com.umutsaydam.deardiary.data.remote.dto.TokenDto
import com.umutsaydam.deardiary.domain.entity.TokenEntity

object TokenMapper {

    fun TokenEntity.toDto(): TokenDto {
        return TokenDto(token = this.token)
    }

    fun TokenDto.toEntity(): TokenEntity {
        return TokenEntity(token = this.token)
    }
}