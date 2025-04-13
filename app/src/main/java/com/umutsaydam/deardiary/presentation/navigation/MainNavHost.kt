package com.umutsaydam.deardiary.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.umutsaydam.deardiary.presentation.addDiary.AddDiaryScreen
import com.umutsaydam.deardiary.presentation.diaries.DiariesScreen
import com.umutsaydam.deardiary.presentation.insights.InsightsScreen
import com.umutsaydam.deardiary.presentation.settings.SettingsScreen
import com.umutsaydam.deardiary.presentation.settings.fingerPrintSettings.SetFingerPrintScreen
import com.umutsaydam.deardiary.presentation.settings.pinSettings.PinSettingsScreen
import com.umutsaydam.deardiary.presentation.settings.pinSettings.SetPinScreen

@Composable
fun MainNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Route.Diaries.route
    ) {
        composable(Route.Diaries.route) {
            DiariesScreen(navController)
        }

        composable(Route.Insights.route) {
            InsightsScreen(navController)
        }

        composable(Route.Settings.route) {
            SettingsScreen(navController)
        }

        composable(Route.AddDiary.route) {
            AddDiaryScreen(navController)
        }

        composable(Route.PinSettings.route) {
            PinSettingsScreen(navController)
        }

        composable(Route.SetPin.route) {
            SetPinScreen(navController)
        }

        composable(Route.SetFingerPrint.route) {
            SetFingerPrintScreen(navController)
        }
    }
}