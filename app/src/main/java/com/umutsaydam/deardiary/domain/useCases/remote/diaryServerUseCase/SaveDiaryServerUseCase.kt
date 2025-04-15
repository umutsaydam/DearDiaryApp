package com.umutsaydam.deardiary.domain.useCases.remote.diaryServerUseCase

import com.umutsaydam.deardiary.domain.Resource
import com.umutsaydam.deardiary.domain.entity.DiaryEntity
import com.umutsaydam.deardiary.domain.repository.DiaryRepository
import javax.inject.Inject

class SaveDiaryServerUseCase @Inject constructor(
    private val diaryRepository: DiaryRepository
) {
    suspend operator fun invoke(diaryEntity: DiaryEntity): Resource<DiaryEntity> {
        return diaryRepository.saveDiary(diaryEntity)
    }
}