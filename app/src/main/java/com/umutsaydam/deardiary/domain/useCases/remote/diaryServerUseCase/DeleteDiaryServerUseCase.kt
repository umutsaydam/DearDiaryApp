package com.umutsaydam.deardiary.domain.useCases.remote.diaryServerUseCase

import com.umutsaydam.deardiary.domain.sealedStates.Resource
import com.umutsaydam.deardiary.domain.repository.DiaryRepository
import java.util.UUID
import javax.inject.Inject

class DeleteDiaryServerUseCase @Inject constructor(
    private val diaryRepository: DiaryRepository
) {
    suspend operator fun invoke(diaryId: UUID): Resource<Unit> {
        return diaryRepository.deleteDiary(diaryId)
    }
}