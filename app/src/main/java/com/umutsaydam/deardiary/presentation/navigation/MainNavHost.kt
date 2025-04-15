package com.umutsaydam.deardiary.presentation.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.umutsaydam.deardiary.domain.entity.DiaryEntity
import com.umutsaydam.deardiary.presentation.addDiary.AddDiaryScreen
import com.umutsaydam.deardiary.presentation.auth.AuthScreen
import com.umutsaydam.deardiary.presentation.diaries.DiariesScreen
import com.umutsaydam.deardiary.presentation.insights.InsightsScreen
import com.umutsaydam.deardiary.presentation.readDiary.ReadDiaryScreen
import com.umutsaydam.deardiary.presentation.settings.SettingsScreen
import com.umutsaydam.deardiary.presentation.settings.fingerPrintSettings.SetFingerPrintScreen
import com.umutsaydam.deardiary.presentation.settings.pinSettings.PinSettingsScreen
import com.umutsaydam.deardiary.presentation.settings.pinSettings.SetPinScreen
import kotlinx.serialization.json.Json

@Composable
fun MainNavHost(mainDestination: String) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = mainDestination
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

        composable(Route.Auth.route) {
            AuthScreen(navController)
        }

        composable(
            Route.ReadDiary.route,
            arguments = listOf(navArgument("diaryJson") { type = NavType.StringType })
        ) {
            val encodedJson = it.arguments?.getString("diaryJson")
            val json = encodedJson?.let { value -> Uri.decode(value) }
            val diary = json?.let { value -> Json.decodeFromString<DiaryEntity>(value) }

            diary?.let { value ->
                ReadDiaryScreen(navController, value)
            }
        }
    }
}