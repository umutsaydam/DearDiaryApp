package com.umutsaydam.deardiary.presentation.settings.pinSettings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.umutsaydam.deardiary.R
import com.umutsaydam.deardiary.presentation.navigation.Route
import com.umutsaydam.deardiary.util.popBackStackOrIgnore
import com.umutsaydam.deardiary.util.safeNavigate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PinSettingsScreen(
    navController: NavHostController
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Set a pin") },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStackOrIgnore()
                        },
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_arrow_back_filled),
                            contentDescription = "Back to the previous screen"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors().copy(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    titleContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    actionIconContentColor = MaterialTheme.colorScheme.primary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = paddingValues.calculateTopPadding() + 8.dp,
                    bottom = paddingValues.calculateBottomPadding()
                )
        ) {
            ListItem(
                modifier = Modifier.clickable {
                    navController.safeNavigate(Route.SetPin.route)
                },
                headlineContent = { Text("Add a pin") },
                supportingContent = { Text("Keep your diaries in secure by adding a pin.") },
                leadingContent = {
                    Icon(
                        painter = painterResource(R.drawable.ic_pin_outline),
                        contentDescription = "Set font family and size."
                    )
                }
            )

            ListItem(
                modifier = Modifier.clickable {

                },
                headlineContent = { Text("Add fingerprint") },
                supportingContent = { Text("Keep your diaries in secure by adding a fingerprint.") },
                leadingContent = {
                    Icon(
                        painter = painterResource(R.drawable.ic_finger_print),
                        contentDescription = "Set font family and size."
                    )
                }
            )
        }
    }
}