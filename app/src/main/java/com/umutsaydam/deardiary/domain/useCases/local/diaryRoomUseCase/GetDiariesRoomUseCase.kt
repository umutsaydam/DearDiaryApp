package com.umutsaydam.deardiary.domain.useCases.local.diaryRoomUseCase

import com.umutsaydam.deardiary.data.local.db.DiaryDao
import com.umutsaydam.deardiary.domain.entity.DiaryEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDiariesRoomUseCase @Inject constructor(
    private val diaryDao: DiaryDao
) {
    operator fun invoke(): Flow<List<DiaryEntity>> {
        return diaryDao.getDiaries()
    }
}