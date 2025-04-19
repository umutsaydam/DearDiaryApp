package com.umutsaydam.deardiary.data.remote.repository

import com.umutsaydam.deardiary.data.remote.DearDiaryApiService
import com.umutsaydam.deardiary.data.remote.mapper.DiaryEmotionMapper.toEntity
import com.umutsaydam.deardiary.data.remote.mapper.TotalInsightsMapper.toEntity
import com.umutsaydam.deardiary.domain.sealedStates.Resource
import com.umutsaydam.deardiary.domain.entity.DiaryEmotionEntity
import com.umutsaydam.deardiary.domain.entity.TotalInsightsEntity
import com.umutsaydam.deardiary.domain.repository.InsightsRepository
import javax.inject.Inject

class InsightsRepositoryImpl @Inject constructor(
    private val dearDiaryApiService: DearDiaryApiService
) : InsightsRepository {

    override suspend fun getTotalInsights(): Resource<TotalInsightsEntity> {
        val result = dearDiaryApiService.getTotalInsights()
        // 401 Token is wrong or expired.
        // 200 TotalInsightsEntity.

        if (result.code() == 200) {
            result.body()?.let {
                return Resource.Success(it.toEntity())
            }
        } else if (result.code() == 401) {
            return Resource.Error(401, "You need to resign in.")
        }
        return Resource.Error()
    }

    override suspend fun getTotalEmotionInsights(timeRange: String): Resource<List<DiaryEmotionEntity>> {
        val result = dearDiaryApiService.getTotalEmotionInsights(timeRange)
        // 401 Token is wrong or expired.
        // 200 List<DiaryEmotionEntity>.

        if (result.code() == 200) {
            result.body()?.let { body ->
                val entityList = body.map { it.toEntity() }
                return Resource.Success(entityList)
            }
        } else if (result.code() == 401) {
            return Resource.Error(401, "You need to resign in.")
        }
        return Resource.Error()
    }
}