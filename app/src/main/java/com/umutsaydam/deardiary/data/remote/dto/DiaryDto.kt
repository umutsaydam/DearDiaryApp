package com.umutsaydam.deardiary.data.remote.dto

import com.google.gson.annotations.SerializedName
import java.util.UUID

data class DiaryDto(
    @SerializedName("diary_id")
    val diaryId: UUID? = null,
    @SerializedName("diary_content")
    val diaryContent: String,
    @SerializedName("diary_date")
    val diaryDate: String? = null,
    @SerializedName("diary_emotion")
    val diaryEmotion: Int? = null
)