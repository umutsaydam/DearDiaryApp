package com.umutsaydam.deardiary.data.remote.mapper

import com.umutsaydam.deardiary.data.remote.dto.TotalInsightsDto
import com.umutsaydam.deardiary.domain.entity.TotalInsightsEntity

object TotalInsightsMapper {

    fun TotalInsightsEntity.toDto(): TotalInsightsDto {
        return TotalInsightsDto(
            totalDiaries = this.totalDiaries,
            currentStreak = this.currentStreak,
            longestStreak = this.longestStreak
        )
    }

    fun TotalInsightsDto.toEntity(): TotalInsightsEntity {
        return TotalInsightsEntity(
            totalDiaries = this.totalDiaries,
            currentStreak = this.currentStreak,
            longestStreak = this.longestStreak
        )
    }
}