package com.umutsaydam.deardiary.presentation.navigation

sealed class Route(
    val route: String
) {
    data object Insights: Route("Insights")
    data object Diaries: Route("Diaries")
    data object Settings: Route("Settings")
    data object AddDiary: Route("AddDiary")
    data object PinSettings: Route("PinSettings")
    data object SetPin: Route("SetPin")
}
