package com.umutsaydam.deardiary.presentation.settings

import android.os.Build
import android.util.Log
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import com.umutsaydam.deardiary.R
import com.umutsaydam.deardiary.presentation.common.BaseAlertDialog
import com.umutsaydam.deardiary.presentation.common.BaseListItem
import com.umutsaydam.deardiary.presentation.common.BaseScaffold
import com.umutsaydam.deardiary.presentation.common.MainNavigationAppBar
import com.umutsaydam.deardiary.presentation.navigation.Route
import com.umutsaydam.deardiary.presentation.settings.fontSettings.FontSettingsDialog
import com.umutsaydam.deardiary.util.safeNavigate
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavHostController) {
    var isReminderTimeOpen by remember { mutableStateOf(false) }
    val calendar = Calendar.getInstance()
    val timePickerState = rememberTimePickerState(
        initialHour = calendar.get(Calendar.HOUR_OF_DAY),
        initialMinute = calendar.get(Calendar.MINUTE),
        is24Hour = true
    )

    var isFontSettingsOpen by remember { mutableStateOf(false) }

    var defaultTextStyle by remember { mutableIntStateOf(0) }
    var defaultFontFamily by remember { mutableIntStateOf(0) }

    var isLogOutDialogOpen by remember { mutableStateOf(false) }

    BaseScaffold(
        title = "Diaries",
        bottomBar = {
            MainNavigationAppBar(navController)
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
            if (isReminderTimeOpen) {
                ReminderTimePicker(
                    timePickerState = timePickerState,
                    onSelected = { time ->

                    },
                    onDismissed = { isReminderTimeOpen = false }
                )
            }

            if (isFontSettingsOpen) {
                FontSettingsDialog(
                    defaultTextStyle = defaultTextStyle,
                    defaultFontFamily = defaultFontFamily,
                    onSelected = { styleIndex, fontIndex ->
                        Log.i("R/T", "selected $styleIndex, $fontIndex")
                        defaultTextStyle = styleIndex
                        defaultFontFamily = fontIndex
                    },
                    onDismissed = { isFontSettingsOpen = false }
                )
            }

            if (isLogOutDialogOpen) {
                showLogoutDialog(
                    onConfirm = { isLogOutDialogOpen = false },
                    onDismissed = { isLogOutDialogOpen = false }
                )
            }

            BaseListItem(
                title = "Daily Reminder",
                description = "Set daily reminders.",
                onClick = { isReminderTimeOpen = true },
                iconRes = R.drawable.ic_notification_outline,
                contentDesc = "Daily Reminder icon"
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
        contentDesc = "Logo out",
        title = "Log out",
        text = { Text("Log out") },
        onDismissed = { onDismissed() },
        confirmButton = {
            TextButton(onClick = { onConfirm() }) {
                Text(
                    text = "You are about to log out of your account, are you sure?",
                    color = MaterialTheme.colorScheme.primary
                )
            }
        },
        dismissButton = {
            TextButton(
                modifier = Modifier.padding(end = 8.dp),
                onClick = { onConfirm() }
            ) { Text(text = "Cancel", color = MaterialTheme.colorScheme.primary) }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReminderTimePicker(
    timePickerState: TimePickerState,
    onSelected: (String) -> Unit,
    onDismissed: () -> Unit
) {
    Dialog(
        onDismissRequest = { onDismissed() },
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(
                    MaterialTheme.colorScheme.surface
                )
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
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
                    modifier = Modifier.padding(end = 8.dp),
                    onClick = { onDismissed() }
                ) {
                    Text(
                        text = "Cancel",
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                TextButton(
                    modifier = Modifier.padding(end = 8.dp),
                    onClick = {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            val formatter = DateTimeFormatter.ofPattern("HH:mm")
                            val timeSelected =
                                LocalTime.of(timePickerState.hour, timePickerState.minute)
                                    .format(formatter)
                            onSelected(timeSelected)
                        } else {
                            val calendar = Calendar.getInstance()
                            calendar.set(Calendar.HOUR_OF_DAY, timePickerState.hour)
                            calendar.set(Calendar.MINUTE, timePickerState.minute)

                            val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
                            val timeSelected = formatter.format(calendar.time)
                            onSelected(timeSelected)
                        }
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