package com.umutsaydam.deardiary.data.remote.repository

import com.umutsaydam.deardiary.data.remote.DearDiaryApiService
import com.umutsaydam.deardiary.data.remote.dto.DiaryDto
import com.umutsaydam.deardiary.data.remote.mapper.DiaryMapper.toDto
import com.umutsaydam.deardiary.data.remote.mapper.DiaryMapper.toEntity
import com.umutsaydam.deardiary.data.remote.mapper.DiaryMapper.toNullUuidDto
import com.umutsaydam.deardiary.domain.Resource
import com.umutsaydam.deardiary.domain.entity.DiaryEntity
import com.umutsaydam.deardiary.domain.repository.DiaryRepository
import java.util.UUID
import javax.inject.Inject

class DiaryRepositoryImpl @Inject constructor(
    private val dearDiaryApiService: DearDiaryApiService
) : DiaryRepository {

    override suspend fun getAllDiaries(): Resource<List<DiaryEntity>> {
        val response = dearDiaryApiService.getDiaries()
        // 401 Token is wrong or expired.
        // 200 List<DiaryEntity>.


        if (response.code() == 200) {
            response.body()?.let { diaryDtoList ->
                val listDiaryEntity = diaryDtoList.map { dto -> dto.toEntity() }
                return Resource.Success(listDiaryEntity)
            }
        } else if (response.code() == 401) {
            return Resource.Error(401, "You need to resign in.")
        }
        return Resource.Error()
    }

    override suspend fun saveDiary(diaryEntity: DiaryEntity): Resource<DiaryEntity> {
        val response = dearDiaryApiService.saveDiary(diaryEntity.toNullUuidDto())
        // 401 Token is wrong or expired.
        // 403 DiaryAlreadyExistException.
        // 201 DiaryEntity.

        if (response.code() == 201) {
            response.body()?.let {
                return Resource.Success(it.toEntity())
            }
        } else if (response.code() == 401) {
            return Resource.Error(401, "You need to resign in.")
        } else if (response.code() == 403) {
            return Resource.Error(403, "You have already added at the same date.")
        }
        return Resource.Error()
    }

    override suspend fun deleteDiary(diaryId: UUID): Resource<Unit> {
        val response = dearDiaryApiService.deleteDiary(diaryId)
        // 200 Ok.

        if (response.code() == 200) {
            return Resource.Success()
        }
        return Resource.Error()
    }

    override suspend fun updateDiary(diaryEntity: DiaryEntity): Resource<DiaryEntity> {
        val response = dearDiaryApiService.updateDiary(diaryEntity.toDto())
        // 401 Token is wrong or expired.
        // 404 Diary not found.
        // 200 DiaryEntity.

        if (response.code() == 200) {
            response.body()?.let {
                return Resource.Success(it.toEntity())
            }
        } else if (response.code() == 401) {
            return Resource.Error(response.code(), response.message())
        } else if (response.code() == 404) {
            return Resource.Error(response.code(), response.message())
        }
        return Resource.Error()
    }
}