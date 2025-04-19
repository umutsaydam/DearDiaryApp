package com.umutsaydam.deardiary.data.remote.dto

data class TotalInsightsDto(
    val totalDiaries: Int,
    val currentStreak: Int,
    val longestStreak: Int
)