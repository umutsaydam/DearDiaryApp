package com.umutsaydam.deardiary.presentation

import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.umutsaydam.deardiary.R
import com.umutsaydam.deardiary.presentation.navigation.Route
import com.umutsaydam.deardiary.util.safeNavigate

@Composable
fun MainNavigationAppBar(navController: NavController) {
    BottomAppBar(
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
    ) {
        val currDest = navController.currentDestination?.route

        NavigationBarItem(
            selected = currDest.equals(Route.Insights.route),
            onClick = {
                navController.safeNavigate(Route.Insights.route)
            },
            icon = {
                Icon(
                    painter = painterResource(R.drawable.ic_insights_filled),
                    contentDescription = "Go to my insights"
                )
            },
            label = { Text("Insights") },
            alwaysShowLabel = true
        )

        NavigationBarItem(
            selected = currDest.equals(Route.Diaries.route),
            onClick = {
                navController.safeNavigate(Route.Diaries.route)
            },
            icon = {
                Icon(
                    painter = painterResource(R.drawable.ic_folder_outline),
                    contentDescription = "Go to my diaries"
                )
            },
            label = { Text("Diaries") },
            alwaysShowLabel = true
        )

        NavigationBarItem(
            selected = currDest.equals(Route.Settings.route),
            onClick = {
                navController.safeNavigate(Route.Settings.route)
            },
            icon = {
                Icon(
                    painter = painterResource(R.drawable.ic_settings_outline),
                    contentDescription = "Go to settings"
                )
            },
            label = { Text("Settings") },
            alwaysShowLabel = true
        )
    }
}