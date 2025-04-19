package com.umutsaydam.deardiary.data.remote.dto

import com.google.gson.annotations.SerializedName

data class DiaryEmotionDto (
    @SerializedName("emotion_id") val emotionId: Int,
    @SerializedName("emotion_count") val emotionCount: Int
)