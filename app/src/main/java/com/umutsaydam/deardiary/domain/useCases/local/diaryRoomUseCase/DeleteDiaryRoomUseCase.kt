package com.umutsaydam.deardiary.domain.useCases.local.diaryRoomUseCase

import com.umutsaydam.deardiary.data.local.db.DiaryDao
import com.umutsaydam.deardiary.domain.entity.DiaryEntity
import javax.inject.Inject

class DeleteDiaryRoomUseCase @Inject constructor(
    private val diaryDao: DiaryDao
) {
    suspend operator fun invoke(diaryEntity: DiaryEntity) {
        diaryDao.deleteDiary(diaryEntity)
    }
}