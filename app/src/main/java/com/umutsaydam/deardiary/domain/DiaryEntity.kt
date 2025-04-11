package com.umutsaydam.deardiary.domain

import java.util.Date
import java.util.UUID

data class DiaryEntity(
    val diaryId: UUID,
    val diaryContent: String,
    val diaryDate: Date,
    val diaryEmotion: Int
)
