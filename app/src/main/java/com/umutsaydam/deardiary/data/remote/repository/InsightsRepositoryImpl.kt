package com.umutsaydam.deardiary.data.remote.repository

import com.umutsaydam.deardiary.R
import com.umutsaydam.deardiary.data.remote.DearDiaryApiService
import com.umutsaydam.deardiary.data.remote.mapper.DiaryEmotionMapper.toEntity
import com.umutsaydam.deardiary.data.remote.mapper.TotalInsightsMapper.toEntity
import com.umutsaydam.deardiary.data.remote.safeApiCall
import com.umutsaydam.deardiary.domain.sealedStates.Resource
import com.umutsaydam.deardiary.domain.entity.DiaryEmotionEntity
import com.umutsaydam.deardiary.domain.entity.TotalInsightsEntity
import com.umutsaydam.deardiary.domain.repository.InsightsRepository
import com.umutsaydam.deardiary.domain.useCases.IsInternetAvailableUseCase
import javax.inject.Inject

class InsightsRepositoryImpl @Inject constructor(
    private val dearDiaryApiService: DearDiaryApiService,
    private val isInternetAvailableUseCase: IsInternetAvailableUseCase
) : InsightsRepository {

    override suspend fun getTotalInsights(): Resource<TotalInsightsEntity> {
        // 401 Token is wrong or expired.
        // 200 TotalInsightsEntity.
        return safeApiCall(
            isInternetAvailable = isInternetAvailableUseCase(),
            apiCall = { dearDiaryApiService.getTotalInsights() },
            successCode = 200,
            errorMessages = mapOf(
                401 to R.string.need_resign
            ),
            map = { totalInsightsDto ->
                totalInsightsDto.toEntity()
            }
        )
    }

    override suspend fun getTotalEmotionInsights(timeRange: String): Resource<List<DiaryEmotionEntity>> {
        // 401 Token is wrong or expired.
        // 200 List<DiaryEmotionEntity>.
        return safeApiCall(
            isInternetAvailable = isInternetAvailableUseCase(),
            apiCall = { dearDiaryApiService.getTotalEmotionInsights(timeRange) },
            successCode = 200,
            errorMessages = mapOf(
                401 to R.string.need_resign
            ),
            map = { dtoList ->
                dtoList.map { it.toEntity() }
            }
        )
    }
}