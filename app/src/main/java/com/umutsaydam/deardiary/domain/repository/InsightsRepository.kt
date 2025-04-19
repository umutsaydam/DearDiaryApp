package com.umutsaydam.deardiary.domain.repository

import com.umutsaydam.deardiary.domain.sealedStates.Resource
import com.umutsaydam.deardiary.domain.entity.DiaryEmotionEntity
import com.umutsaydam.deardiary.domain.entity.TotalInsightsEntity

interface InsightsRepository {
    suspend fun getTotalInsights(): Resource<TotalInsightsEntity>
    suspend fun getTotalEmotionInsights(timeRange: String): Resource<List<DiaryEmotionEntity>>
}