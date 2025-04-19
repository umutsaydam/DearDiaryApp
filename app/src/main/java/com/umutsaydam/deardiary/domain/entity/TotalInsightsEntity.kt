package com.umutsaydam.deardiary.domain.entity

data class TotalInsightsEntity(
    val totalDiaries: Int,
    val currentStreak: Int,
    val longestStreak: Int
)