package com.umutsaydam.deardiary.data.remote.mapper

import com.umutsaydam.deardiary.data.remote.dto.DiaryEmotionDto
import com.umutsaydam.deardiary.domain.entity.DiaryEmotionEntity

object DiaryEmotionMapper {

    fun DiaryEmotionEntity.toDto(): DiaryEmotionDto {
        return DiaryEmotionDto(
            emotionId = this.emotionId,
            emotionCount = this.emotionCount
        )
    }

    fun DiaryEmotionDto.toEntity(): DiaryEmotionEntity {
        return DiaryEmotionEntity(
            emotionId = this.emotionId,
            emotionCount = this.emotionCount
        )
    }
}