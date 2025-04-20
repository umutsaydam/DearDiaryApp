package com.umutsaydam.deardiary.presentation.common

import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
                    contentDescription = stringResource(R.string.go_insights)
                )
            },
            label = { Text(stringResource(R.string.insights)) },
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
                    contentDescription = stringResource(R.string.go_diaries)
                )
            },
            label = { Text(stringResource(R.string.diaries)) },
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
                    contentDescription = stringResource(R.string.go_settings)
                )
            },
            label = { Text(stringResource(R.string.settings)) },
            alwaysShowLabel = true
        )
    }
}