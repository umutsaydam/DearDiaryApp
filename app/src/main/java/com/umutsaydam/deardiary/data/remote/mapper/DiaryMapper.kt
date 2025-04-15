package com.umutsaydam.deardiary.data.remote.mapper

import com.umutsaydam.deardiary.data.remote.dto.DiaryDto
import com.umutsaydam.deardiary.domain.entity.DiaryEntity

object DiaryMapper {

    fun DiaryEntity.toDto(): DiaryDto {
        return DiaryDto(
            diaryId = this.diaryId,
            diaryContent = this.diaryContent,
            diaryDate = this.diaryDate,
            diaryEmotion = this.diaryEmotion
        )
    }

    fun DiaryEntity.toNullUuidDto(): DiaryDto {
        return DiaryDto(
            diaryContent = this.diaryContent,
            diaryDate = this.diaryDate,
            diaryEmotion = this.diaryEmotion
        )
    }

    fun DiaryDto.toEntity(): DiaryEntity {
        return DiaryEntity(
            diaryId = this.diaryId!!,
            diaryContent = this.diaryContent,
            diaryDate = this.diaryDate!!,
            diaryEmotion = this.diaryEmotion!!
        )
    }
}