package com.umutsaydam.deardiary.presentation.settings

import android.os.Build
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import com.umutsaydam.deardiary.R
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

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Diaries") },
                actions = {
                    IconButton(
                        onClick = {

                        },

                        ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search in diaries"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors().copy(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    titleContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    actionIconContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
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
                    onConfirm = {
                        isLogOutDialogOpen = false
                    },
                    onDismissed = {
                        isLogOutDialogOpen = false
                    }
                )
            }

            ListItem(
                modifier = Modifier.clickable {
                    isReminderTimeOpen = true
                },
                headlineContent = { Text("Daily Reminder") },
                supportingContent = { Text("Set daily reminders.") },
                leadingContent = {
                    Icon(
                        painter = painterResource(R.drawable.ic_notification_outline),
                        contentDescription = "Set daily reminders."
                    )
                }
            )

            ListItem(
                modifier = Modifier.clickable {
                    isFontSettingsOpen = true
                },
                headlineContent = { Text("Font Family and Size") },
                supportingContent = { Text("Set font family and size.") },
                leadingContent = {
                    Icon(
                        painter = painterResource(R.drawable.ic_text_filled),
                        contentDescription = "Set font family and size."
                    )
                }
            )

            ListItem(
                modifier = Modifier.clickable {
                    navController.safeNavigate(Route.PinSettings.route)
                },
                headlineContent = { Text("Set a Pin") },
                supportingContent = { Text("Set a pin and keep your diaries in secure.") },
                leadingContent = {
                    Icon(
                        painter = painterResource(R.drawable.ic_lock_outline),
                        contentDescription = "Set font family and size."
                    )
                }
            )

            ListItem(
                modifier = Modifier.clickable {
                    isLogOutDialogOpen = true
                },
                headlineContent = { Text("Log out") },
                supportingContent = { Text("Log out your account.") },
                leadingContent = {
                    Icon(
                        painter = painterResource(R.drawable.ic_log_out_outline),
                        contentDescription = "Set font family and size."
                    )
                }
            )
        }
    }
}

@Composable
fun showLogoutDialog(
    onConfirm: () -> Unit,
    onDismissed: () -> Unit,
) {
    AlertDialog(
        icon = {
            Icon(
                painter = painterResource(R.drawable.ic_log_out_outline),
                contentDescription = "Log out"
            )
        },
        title = {
            Text(
                "Log out"
            )
        },
        text = {
            Text(
                "You are about to log out of your account, are you sure?"
            )
        },
        onDismissRequest = { onDismissed() },
        confirmButton = {
            TextButton(
                onClick = { onConfirm() }
            ) {
                Text(
                    text = "Log out",
                    color = MaterialTheme.colorScheme.primary
                )
            }
        },
        dismissButton = {
            TextButton(
                modifier = Modifier.padding(end = 8.dp),
                onClick = { onConfirm() }
            ) {
                Text(
                    text = "Cancel",
                    color = MaterialTheme.colorScheme.primary
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        iconContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        titleContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        shape = AlertDialogDefaults.shape
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