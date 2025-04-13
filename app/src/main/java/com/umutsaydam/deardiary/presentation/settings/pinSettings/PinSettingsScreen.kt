package com.umutsaydam.deardiary.presentation.settings.pinSettings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.umutsaydam.deardiary.R
import com.umutsaydam.deardiary.presentation.common.BaseListItem
import com.umutsaydam.deardiary.presentation.common.BaseScaffold
import com.umutsaydam.deardiary.presentation.navigation.Route
import com.umutsaydam.deardiary.util.popBackStackOrIgnore
import com.umutsaydam.deardiary.util.safeNavigate

@Composable
fun PinSettingsScreen(
    navController: NavHostController
) {
    BaseScaffold(
        title = "Set a pin",
        navigation = {
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
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = paddingValues.calculateTopPadding() + 8.dp,
                    bottom = paddingValues.calculateBottomPadding()
                )
        ) {
            BaseListItem(
                title = "Add a pin",
                description = "Keep your diaries in secure by adding a pin.",
                onClick = { navController.safeNavigate(Route.SetPin.route) },
                iconRes = R.drawable.ic_pin_outline,
                contentDesc = "Set font family and size."
            )

            BaseListItem(
                title = "Add fingerprint",
                description = "Keep your diaries in secure by adding a fingerprint.",
                onClick = { navController.safeNavigate(Route.SetFingerPrint.route) },
                iconRes = R.drawable.ic_pin_outline,
                contentDesc = "Add fingerprint icon"
            )
        }
    }
}