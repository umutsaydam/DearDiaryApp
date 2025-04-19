package com.umutsaydam.deardiary.domain.repository

import com.umutsaydam.deardiary.domain.sealedStates.Resource
import com.umutsaydam.deardiary.domain.entity.DiaryEntity
import java.util.UUID

interface DiaryRepository {
    suspend fun getAllDiaries(): Resource<List<DiaryEntity>>

    suspend fun saveDiary(diaryEntity: DiaryEntity): Resource<DiaryEntity>

    suspend fun deleteDiary(diaryId: UUID): Resource<Unit>

    suspend fun updateDiary(diaryEntity: DiaryEntity): Resource<DiaryEntity>
}