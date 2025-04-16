package com.umutsaydam.deardiary.presentation.common

import androidx.compose.foundation.clickable
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource

@Composable
fun BaseListItem(
    title: String,
    description: String,
    onClick: () -> Unit,
    iconRes: Int,
    contentDesc: String,
    trailingContent: @Composable () -> Unit = {}
) {
    ListItem(
        modifier = Modifier.clickable { onClick() },
        headlineContent = { Text(title) },
        supportingContent = { Text(description) },
        leadingContent = {
            Icon(painter = painterResource(iconRes), contentDescription = contentDesc)
        },
        trailingContent = trailingContent
    )
}