package com.umutsaydam.deardiary.data.remote.repository

import com.umutsaydam.deardiary.R
import com.umutsaydam.deardiary.data.remote.DearDiaryApiService
import com.umutsaydam.deardiary.data.remote.mapper.DiaryMapper.toDto
import com.umutsaydam.deardiary.data.remote.mapper.DiaryMapper.toEntity
import com.umutsaydam.deardiary.data.remote.mapper.DiaryMapper.toNullUuidDto
import com.umutsaydam.deardiary.data.remote.safeApiCall
import com.umutsaydam.deardiary.domain.sealedStates.Resource
import com.umutsaydam.deardiary.domain.entity.DiaryEntity
import com.umutsaydam.deardiary.domain.repository.DiaryRepository
import com.umutsaydam.deardiary.domain.useCases.IsInternetAvailableUseCase
import java.util.UUID
import javax.inject.Inject

class DiaryRepositoryImpl @Inject constructor(
    private val dearDiaryApiService: DearDiaryApiService,
    private val isInternetAvailableUseCase: IsInternetAvailableUseCase
) : DiaryRepository {

    override suspend fun getAllDiaries(): Resource<List<DiaryEntity>> {
        // 401 Token is wrong or expired.
        // 200 List<DiaryEntity>.
        return safeApiCall(
            isInternetAvailable = isInternetAvailableUseCase(),
            apiCall = { dearDiaryApiService.getDiaries() },
            successCode = 200,
            errorMessages = mapOf(
                401 to R.string.need_resign
            ),
            map = { dtoList ->
                dtoList.map { it.toEntity() }
            }
        )
    }

    override suspend fun saveDiary(diaryEntity: DiaryEntity): Resource<DiaryEntity> {
        // 401 Token is wrong or expired.
        // 403 DiaryAlreadyExistException.
        // 201 DiaryEntity.
        return safeApiCall(
            isInternetAvailable = isInternetAvailableUseCase(),
            apiCall = { dearDiaryApiService.saveDiary(diaryEntity.toNullUuidDto()) },
            successCode = 201,
            errorMessages = mapOf(
                401 to R.string.need_resign,
                403 to R.string.added_at_same_date
            ),
            map = { savedDiaryDto ->
                savedDiaryDto.toEntity()
            }
        )
    }

    override suspend fun deleteDiary(diaryId: UUID): Resource<Unit> {
        // 401 Token is wrong or expired.
        // 200 Ok.
        return safeApiCall(
            isInternetAvailable = isInternetAvailableUseCase(),
            apiCall = { dearDiaryApiService.deleteDiary(diaryId) },
            successCode = 200,
            errorMessages = mapOf(
                401 to R.string.need_resign
            ),
            map = {}
        )
    }

    override suspend fun updateDiary(diaryEntity: DiaryEntity): Resource<DiaryEntity> {
        // 401 Token is wrong or expired.
        // 404 Diary not found.
        // 200 DiaryEntity.
        return safeApiCall(
            isInternetAvailable = isInternetAvailableUseCase(),
            apiCall = { dearDiaryApiService.updateDiary(diaryEntity.toDto()) },
            successCode = 200,
            errorMessages = mapOf(
                401 to R.string.need_resign,
                404 to R.string.diary_not_found
            ),
            map = { updatedDto ->
                updatedDto.toEntity()
            }
        )
    }
}