package com.umutsaydam.deardiary.presentation.settings.lockSettings

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.umutsaydam.deardiary.R
import com.umutsaydam.deardiary.domain.sealedStates.UiMessage
import com.umutsaydam.deardiary.presentation.Dimens.SpacingSmall
import com.umutsaydam.deardiary.presentation.common.BaseListItem
import com.umutsaydam.deardiary.presentation.common.BaseScaffold
import com.umutsaydam.deardiary.presentation.navigation.Route
import com.umutsaydam.deardiary.util.popBackStackOrIgnore
import com.umutsaydam.deardiary.util.safeNavigate

@Composable
fun PinSettingsScreen(
    navController: NavHostController,
    lockSettingsViewModel: LockSettingsViewModel = hiltViewModel()
) {
    val isFingerPrintEnable by lockSettingsViewModel.isFingerPrintEnabled.collectAsState()
    val isPinEnable by lockSettingsViewModel.isPinEnable.collectAsState()
    val uiMessageState = lockSettingsViewModel.uiMessageState.collectAsState().value
    val context = LocalContext.current

    LaunchedEffect(uiMessageState) {
        when (uiMessageState) {
            is UiMessage.Success -> {
                Toast.makeText(
                    context,
                    context.getString(uiMessageState.message),
                    Toast.LENGTH_SHORT
                )
                    .show()
                lockSettingsViewModel.clearUiMessageState()
            }

            is UiMessage.Error -> {
                Toast.makeText(
                    context,
                    context.getString(uiMessageState.message),
                    Toast.LENGTH_SHORT
                )
                    .show()
                lockSettingsViewModel.clearUiMessageState()
            }

            else -> {}
        }
    }


    BaseScaffold(
        title = { Text(stringResource(R.string.set_pin)) },
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
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = paddingValues.calculateTopPadding() + SpacingSmall,
                    bottom = paddingValues.calculateBottomPadding()
                )
        ) {
            BaseListItem(
                title = stringResource(R.string.add_pin),
                description = stringResource(R.string.keep_diaries_secure_with_pin),
                onClick = {
                    if (lockSettingsViewModel.navigateSetPinScreenIfFingerprintNotEnable()) {
                        navController.safeNavigate(Route.SetPin.route)
                    } else {
                        navController.safeNavigate(Route.SetFingerPrint.route)
                    }
                },
                iconRes = R.drawable.ic_pin_outline,
                contentDesc = stringResource(R.string.select_font_family_size),
                trailingContent = {
                    Switch(
                        checked = isPinEnable,
                        onCheckedChange = {
                            if (lockSettingsViewModel.navigateSetPinScreenIfFingerprintNotEnable()) {
                                navController.safeNavigate(Route.SetPin.route)
                            } else {
                                navController.safeNavigate(Route.SetFingerPrint.route)
                            }
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = MaterialTheme.colorScheme.primary,
                            checkedTrackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                            uncheckedThumbColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant,
                        )
                    )
                }
            )

            BaseListItem(
                title = stringResource(R.string.add_fingerprint),
                description = stringResource(R.string.keep_diaries_secure_with_fingerprint),
                onClick = {
                    if (lockSettingsViewModel.navigateSetFingerprintIfPinEnable()) {
                        navController.safeNavigate(Route.SetFingerPrint.route)
                    } else {
                        navController.safeNavigate(Route.SetPin.route)
                    }
                },
                iconRes = R.drawable.ic_finger_print,
                contentDesc = stringResource(R.string.add_fingerprint),
                trailingContent = {
                    Switch(
                        checked = isFingerPrintEnable,
                        onCheckedChange = {
                            if (lockSettingsViewModel.navigateSetFingerprintIfPinEnable()) {
                                navController.safeNavigate(Route.SetFingerPrint.route)
                            } else {
                                navController.safeNavigate(Route.SetPin.route)
                            }
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = MaterialTheme.colorScheme.primary,
                            checkedTrackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                            uncheckedThumbColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant,
                        )
                    )
                }
            )
        }
    }
}