package com.umutsaydam.deardiary.domain.useCases.local.diaryRoomUseCase

import com.umutsaydam.deardiary.data.local.db.DiaryDao
import com.umutsaydam.deardiary.domain.entity.DiaryEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchDiaryRoomUseCase @Inject constructor(
    private val diaryDao: DiaryDao
) {
    operator fun invoke(query: String): Flow<List<DiaryEntity>> {
        return diaryDao.searchDiaries(query)
    }
}