package com.umutsaydam.deardiary.domain.useCases.remote.insightsUseCase

import com.umutsaydam.deardiary.domain.sealedStates.Resource
import com.umutsaydam.deardiary.domain.entity.TotalInsightsEntity
import com.umutsaydam.deardiary.domain.repository.InsightsRepository
import javax.inject.Inject

class GetTotalInsightsUseCase @Inject constructor(
    private val insightsRepository: InsightsRepository
) {
    suspend operator fun invoke(): Resource<TotalInsightsEntity> {
        return insightsRepository.getTotalInsights()
    }
}