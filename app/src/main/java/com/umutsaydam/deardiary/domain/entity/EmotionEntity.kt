package com.umutsaydam.deardiary.domain.entity

import com.umutsaydam.deardiary.R

data class EmotionEntity(
    val emotionId: Int,
    val emotionContent: String,
    val emotionSource: Int
)

val emotionList = listOf(
    EmotionEntity(0, "Sad", R.raw.anim_sad),
    EmotionEntity(1, "Joy", R.raw.anim_joy),
    EmotionEntity(2, "Love", R.raw.anim_love),
    EmotionEntity(3, "Anger", R.raw.anim_anger),
    EmotionEntity(4, "Fear", R.raw.anim_fear),
    EmotionEntity(5, "Surprise", R.raw.anim_surprise),
)