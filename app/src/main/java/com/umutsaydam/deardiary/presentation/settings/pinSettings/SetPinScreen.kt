package com.umutsaydam.deardiary.presentation.settings.pinSettings

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.umutsaydam.deardiary.R
import com.umutsaydam.deardiary.domain.PinStateEnum
import com.umutsaydam.deardiary.presentation.common.BaseScaffold
import com.umutsaydam.deardiary.presentation.common.PinPanel
import com.umutsaydam.deardiary.util.Constants.PIN_LENGTH
import com.umutsaydam.deardiary.util.popBackStackOrIgnore

@Composable
fun SetPinScreen(
    navController: NavHostController,
    pinSettingsViewModel: PinSettingsViewModel = hiltViewModel()
) {
    val pinText = pinSettingsViewModel.pinText.collectAsState()
    val pin = pinText.value.map { it.toString().toInt() }
    val pinTextConfirm = pinSettingsViewModel.pinTextConfirm.collectAsState()
    val pinConfirm = pinTextConfirm.value.map { it.toString().toInt() }
    val maxLength = PIN_LENGTH
    val pinState = pinSettingsViewModel.pinState.collectAsState().value
    val toastMessage = pinSettingsViewModel.uiMessageState.collectAsState().value
    val context = LocalContext.current

    LaunchedEffect(toastMessage) {
        if (toastMessage.isNotEmpty()) {
            Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
            pinSettingsViewModel.clearUiMessageState()
        }
    }

    pinState?.let {
        BaseScaffold(
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
            },
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceContainerLowest)
                    .padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(it.message)

                when (it) {
                    PinStateEnum.ENTER_CURRENT_PIN -> {
                        PinPanel(
                            pin = pin,
                            maxLength = maxLength,
                            pinText = pinText.value,
                            onValueChange = { value ->
                                pinSettingsViewModel.updatePinText(value)
                            }
                        )
                    }

                    PinStateEnum.ENTER_FIRST -> {
                        PinPanel(
                            pin = pin,
                            maxLength = maxLength,
                            pinText = pinText.value,
                            onValueChange = { value ->
                                pinSettingsViewModel.updatePinText(value)
                            }
                        )
                    }

                    PinStateEnum.CONFIRM_PIN -> {
                        PinPanel(
                            pin = pinConfirm,
                            maxLength = maxLength,
                            pinText = pinTextConfirm.value,
                            onValueChange = { value ->
                                pinSettingsViewModel.updatePinTextConfirm(value)
                            }
                        )
                    }

                    PinStateEnum.DONE -> {
                        navController.popBackStackOrIgnore()
                    }
                }
            }
        }
    }
}