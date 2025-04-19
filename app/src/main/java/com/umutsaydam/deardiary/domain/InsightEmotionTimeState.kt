package com.umutsaydam.deardiary.domain

sealed class InsightEmotionTimeState(val insightContent: String, val insightParam: String) {
    data object AllTime : InsightEmotionTimeState("All time", "all")
    data object ThisMonth : InsightEmotionTimeState("This month", "this_month")
    data object LastWeek : InsightEmotionTimeState("Last week", "last_week")
    data object ThisWeek : InsightEmotionTimeState("This week", "this_week")
}

val insightEmotionList = listOf(
    InsightEmotionTimeState.AllTime,
    InsightEmotionTimeState.ThisMonth,
    InsightEmotionTimeState.LastWeek,
    InsightEmotionTimeState.ThisWeek,
)