package com.umutsaydam.deardiary.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.umutsaydam.deardiary.presentation.diary.DiariesScreen
import com.umutsaydam.deardiary.presentation.insights.InsightsScreen
import com.umutsaydam.deardiary.presentation.settings.SettingsScreen

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
    }
}