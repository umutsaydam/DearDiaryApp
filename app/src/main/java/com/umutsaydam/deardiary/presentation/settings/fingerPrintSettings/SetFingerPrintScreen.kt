package com.umutsaydam.deardiary.presentation.settings.fingerPrintSettings

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.umutsaydam.deardiary.R
import com.umutsaydam.deardiary.presentation.Dimens.SizeIconSmall
import com.umutsaydam.deardiary.presentation.Dimens.SizeImageLarge
import com.umutsaydam.deardiary.presentation.common.BaseScaffold
import com.umutsaydam.deardiary.presentation.common.BiometricManagerLaunchEffect
import com.umutsaydam.deardiary.util.popBackStackOrIgnore

@Composable
fun SetFingerPrintScreen(
    navController: NavHostController,
    fingerPrintViewModel: FingerPrintViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val toastMessage = fingerPrintViewModel.uiMessageState.collectAsState().value

    LaunchedEffect(toastMessage) {
        if (toastMessage.isNotEmpty()) {
            Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
            fingerPrintViewModel.clearUiMessageState()
        }
    }

    BiometricManagerLaunchEffect(
        onSuccess = {
            fingerPrintViewModel.setIsFingerPrintEnabled()
            navController.popBackStackOrIgnore()
        },
        onFailed = {},
        onSensorError = { message ->
            fingerPrintViewModel.updateUiMessageState(message)
        }
    )

    BaseScaffold(
        navigation = {
            IconButton(
                onClick = {
                    navController.popBackStackOrIgnore()
                },
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_back_filled),
                    contentDescription = stringResource(R.string.back_prev_screen)
                )
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
                contentDescription = stringResource(R.string.fingerprint),
                modifier = Modifier.size(SizeImageLarge),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(SizeIconSmall))
            Text(stringResource(R.string.scan_fingerprint), style = MaterialTheme.typography.titleMedium)
        }
    }
}