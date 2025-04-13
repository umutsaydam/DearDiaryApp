package com.umutsaydam.deardiary.presentation.common

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource

@Composable
fun BaseAlertDialog(
    modifier: Modifier = Modifier,
    icon: Int, // Resource ID ex. R.drawable.ic_log_out_outline
    contentDesc: String,
    title: String,
    text: @Composable () -> Unit,
    onDismissed: () -> Unit,
    confirmButton: @Composable () -> Unit = {},
    dismissButton: @Composable () -> Unit = {},
) {
    AlertDialog(
        modifier = modifier,
        icon = {
            Icon(
                painter = painterResource(icon),
                contentDescription = contentDesc
            )
        },
        title = { Text(title) },
        text = text,
        onDismissRequest = { onDismissed() },
        confirmButton = confirmButton,
        dismissButton = dismissButton,
        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        iconContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        titleContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        shape = AlertDialogDefaults.shape
    )
}