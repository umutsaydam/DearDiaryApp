package com.umutsaydam.deardiary.domain.useCases.remote.diaryServerUseCase

import com.umutsaydam.deardiary.domain.sealedStates.Resource
import com.umutsaydam.deardiary.domain.entity.DiaryEntity
import com.umutsaydam.deardiary.domain.repository.DiaryRepository
import javax.inject.Inject

class GetDiariesServerUseCase @Inject constructor(
    private val diaryRepository: DiaryRepository
) {
    suspend operator fun invoke(): Resource<List<DiaryEntity>> {
        return diaryRepository.getAllDiaries()
    }
}