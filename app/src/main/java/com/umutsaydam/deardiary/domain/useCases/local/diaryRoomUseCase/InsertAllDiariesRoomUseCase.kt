package com.umutsaydam.deardiary.domain.useCases.local.diaryRoomUseCase

import com.umutsaydam.deardiary.data.local.db.DiaryDao
import com.umutsaydam.deardiary.domain.entity.DiaryEntity
import javax.inject.Inject

class InsertAllDiariesRoomUseCase @Inject constructor(
    private val diaryDao: DiaryDao
) {
    suspend operator fun invoke(diaries: List<DiaryEntity>) {
        diaryDao.insertAllDiaries(diaries)
    }
}