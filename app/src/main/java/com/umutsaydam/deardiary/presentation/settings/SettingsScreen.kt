package com.umutsaydam.deardiary.presentation.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.umutsaydam.deardiary.R
import com.umutsaydam.deardiary.presentation.Dimens.PaddingSmall
import com.umutsaydam.deardiary.presentation.Dimens.PaddingLarge
import com.umutsaydam.deardiary.presentation.Dimens.CornerSmall
import com.umutsaydam.deardiary.presentation.common.BaseAlertDialog
import com.umutsaydam.deardiary.presentation.common.BaseListItem
import com.umutsaydam.deardiary.presentation.common.BaseScaffold
import com.umutsaydam.deardiary.presentation.common.MainNavigationAppBar
import com.umutsaydam.deardiary.presentation.navigation.Route
import com.umutsaydam.deardiary.presentation.reminder.ReminderViewModel
import com.umutsaydam.deardiary.presentation.settings.fontSettings.FontSettingsDialog
import com.umutsaydam.deardiary.util.safeNavigate
import com.umutsaydam.deardiary.util.safeNavigateWithClearingBackStack
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavHostController,
    reminderViewModel: ReminderViewModel = hiltViewModel(),
    settingsViewModel: SettingsViewModel = hiltViewModel()
) {
    val defaultTextStyle by settingsViewModel.defaultFontSize.collectAsState()
    val defaultFontFamily by settingsViewModel.defaultFontFamily.collectAsState()
    val isReminderEnable by reminderViewModel.isReminderEnabled.collectAsState(initial = false)
    var isReminderTimeOpen by remember { mutableStateOf(false) }
    val calendar = Calendar.getInstance()
    val timePickerState = rememberTimePickerState(
        initialHour = calendar.get(Calendar.HOUR_OF_DAY),
        initialMinute = calendar.get(Calendar.MINUTE),
        is24Hour = true
    )
    val context = LocalContext.current

    var isFontSettingsOpen by remember { mutableStateOf(false) }
    var isLogOutDialogOpen by remember { mutableStateOf(false) }
    val isLogout by settingsViewModel.isLogout.collectAsState()

    LaunchedEffect(isLogout) {
        if (isLogout) {
            navController.safeNavigateWithClearingBackStack(Route.Auth.route)
        }
    }

    BaseScaffold(
        title = { Text("Diaries") },
        bottomBar = {
            MainNavigationAppBar(navController)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = paddingValues.calculateTopPadding() + PaddingSmall,
                    bottom = paddingValues.calculateBottomPadding()
                )
        ) {
            if (isReminderTimeOpen) {
                ReminderTimePicker(
                    timePickerState = timePickerState,
                    onSelected = { hour, minute ->
                        reminderViewModel.scheduleReminder(context, hour, minute)
                        reminderViewModel.setReminder(true)
                    },
                    onDismissed = {
                        isReminderTimeOpen = false
                    }
                )
            }

            if (isFontSettingsOpen) {
                FontSettingsDialog(
                    defaultTextStyle = defaultTextStyle,
                    defaultFontFamily = defaultFontFamily,
                    onSelected = { style, font ->
                        settingsViewModel.setSizeAndFamily(style, font)
                    },
                    onDismissed = { isFontSettingsOpen = false },
                    onSave = { textStyle, fontFamily ->
                        settingsViewModel.setFontFamilyAndSize(fontFamily, textStyle)
                        isFontSettingsOpen = false
                    }
                )
            }

            if (isLogOutDialogOpen) {
                showLogoutDialog(
                    onConfirm = {
                        isLogOutDialogOpen = false
                        settingsViewModel.logout()
                    },
                    onDismissed = { isLogOutDialogOpen = false }
                )
            }

            BaseListItem(
                title = "Daily Reminder",
                description = "Set daily reminders.",
                onClick = { isReminderTimeOpen = true },
                iconRes = R.drawable.ic_notification_outline,
                contentDesc = "Daily Reminder icon",
                trailingContent = {
                    Switch(
                        checked = isReminderEnable,
                        onCheckedChange = {
                            if (!isReminderEnable) {
                                isReminderTimeOpen = true
                            } else {
                                reminderViewModel.setReminder(false)
                                reminderViewModel.cancelReminder(context)
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
                title = "Font Family and Size",
                description = "Set font family and size.",
                onClick = { isFontSettingsOpen = true },
                iconRes = R.drawable.ic_text_filled,
                contentDesc = "Font family and size icon"
            )

            BaseListItem(
                title = "Set a Pin",
                description = "Set a pin and keep your diaries in secure.",
                onClick = { navController.safeNavigate(Route.PinSettings.route) },
                iconRes = R.drawable.ic_lock_outline,
                contentDesc = "Set a pin icon"
            )

            BaseListItem(
                title = "Log out",
                description = "Log out your account.",
                onClick = { isLogOutDialogOpen = true },
                iconRes = R.drawable.ic_log_out_outline,
                contentDesc = "Log out icon"
            )
        }
    }
}

@Composable
fun showLogoutDialog(
    onConfirm: () -> Unit,
    onDismissed: () -> Unit,
) {
    BaseAlertDialog(
        icon = R.drawable.ic_log_out_outline,
        contentDesc = "Logout",
        title = "Logout",
        text = { Text("You are about to log out of your account, are you sure?") },
        onDismissed = { onDismissed() },
        confirmButton = {
            TextButton(onClick = { onConfirm() }) {
                Text(
                    text = "Logout",
                    color = MaterialTheme.colorScheme.primary
                )
            }
        },
        dismissButton = {
            TextButton(
                modifier = Modifier.padding(end = PaddingSmall),
                onClick = { onConfirm() }
            ) { Text(text = "Cancel", color = MaterialTheme.colorScheme.primary) }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReminderTimePicker(
    timePickerState: TimePickerState,
    onSelected: (Int, Int) -> Unit,
    onDismissed: () -> Unit
) {
    Dialog(
        onDismissRequest = { onDismissed() },
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .clip(RoundedCornerShape(CornerSmall))
                .background(
                    MaterialTheme.colorScheme.surface
                )
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(PaddingLarge),
                text = "Select time"
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                TimeInput(
                    state = timePickerState,
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(
                    modifier = Modifier.padding(end = PaddingSmall),
                    onClick = { onDismissed() }
                ) {
                    Text(
                        text = "Cancel",
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                TextButton(
                    modifier = Modifier.padding(end = PaddingSmall),
                    onClick = {
                        onSelected(timePickerState.hour, timePickerState.minute)
                        onDismissed()
                    }
                ) {
                    Text(
                        text = "Remind me",
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}