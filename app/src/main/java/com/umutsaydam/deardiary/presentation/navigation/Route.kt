package com.umutsaydam.deardiary.presentation.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Route(
    val route: String,
    val arguments: List<NamedNavArgument> = emptyList()
) {
    data object Insights : Route("Insights")
    data object Diaries : Route("Diaries")
    data object Settings : Route("Settings")
    data object AddDiary : Route("AddDiary")
    data object PinSettings : Route("PinSettings")
    data object SetPin : Route("SetPin")
    data object SetFingerPrint : Route("SetFingerPrint")
    data object Auth : Route("Auth")
    data object ReadDiary : Route("ReadDiary?diaryJson={diaryJson}")
    data object EntryPin : Route(
        route = "EntryPin/{isFingerprintEnabled}",
        arguments = listOf(
            navArgument("isFingerprintEnabled") { type = NavType.BoolType }
        )
    ){
        fun createRoute(isFingerprintEnabled: Boolean) = "EntryPin/$isFingerprintEnabled"
    }
    data object EntryFingerprint: Route("EntryFingerprint")
}
