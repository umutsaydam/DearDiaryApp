package com.umutsaydam.deardiary.domain.useCases.remote.insightsUseCase

import com.umutsaydam.deardiary.domain.sealedStates.Resource
import com.umutsaydam.deardiary.domain.entity.DiaryEmotionEntity
import com.umutsaydam.deardiary.domain.repository.InsightsRepository
import javax.inject.Inject

class GetTotalEmotionInsightsUseCase @Inject constructor(
    private val insightsRepository: InsightsRepository
) {
    suspend operator fun invoke(timeRange: String): Resource<List<DiaryEmotionEntity>> {
        return insightsRepository.getTotalEmotionInsights(timeRange)
    }
}