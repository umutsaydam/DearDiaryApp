package com.umutsaydam.deardiary.presentation.navigation

import androidx.navigation.NavArgument

sealed class Route(
    val route: String,
    val arguments: NavArgument? =  null
) {
    data object Insights: Route("Insights")
    data object Diaries: Route("Diaries")
    data object Settings: Route("Settings")
    data object AddDiary: Route("AddDiary")
    data object PinSettings: Route("PinSettings")
    data object SetPin: Route("SetPin")
    data object SetFingerPrint: Route("SetFingerPrint")
    data object Auth: Route("Auth")
    data object ReadDiary: Route("ReadDiary?diaryJson={diaryJson}")
}
