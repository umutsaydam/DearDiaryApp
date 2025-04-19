package com.umutsaydam.deardiary.presentation.entryFingerprint

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import com.umutsaydam.deardiary.R
import com.umutsaydam.deardiary.presentation.Dimens.SizeImageLarge
import com.umutsaydam.deardiary.presentation.Dimens.SpacingMedium
import com.umutsaydam.deardiary.presentation.common.BaseScaffold
import com.umutsaydam.deardiary.presentation.common.BiometricManagerLaunchEffect
import com.umutsaydam.deardiary.presentation.navigation.Route
import com.umutsaydam.deardiary.util.safeNavigate
import com.umutsaydam.deardiary.util.safeNavigateWithClearingBackStack

@Composable
fun EntryFingerprintScreen(
    navController: NavHostController,
    entryFingerprintViewModel: EntryFingerprintViewModel = EntryFingerprintViewModel()
) {
    val context = LocalContext.current
    val toastMessage = entryFingerprintViewModel.uiMessageState.collectAsState().value

    LaunchedEffect(toastMessage) {
        if (toastMessage.isNotEmpty()) {
            Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
            entryFingerprintViewModel.clearUiMessageState()
        }
    }

    BiometricManagerLaunchEffect(
        onSuccess = {
            navController.safeNavigateWithClearingBackStack(Route.Diaries.route)
        },
        onFailed = {},
        onSensorError = { message ->
            entryFingerprintViewModel.updateUiMessageState(message)
        }
    )

    BaseScaffold(
        topActions = {
            TextButton(
                onClick = {
                    navController.safeNavigate(Route.EntryPin.createRoute(true))
                },
            ) {
                Text("Use pin")
            }
        },
        containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_finger_print),
                contentDescription = "Fingerprint",
                modifier = Modifier.size(SizeImageLarge),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(SpacingMedium))
            Text("Scan your fingerprint.", style = MaterialTheme.typography.titleMedium)
        }
    }
}