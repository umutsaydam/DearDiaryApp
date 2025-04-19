package com.umutsaydam.deardiary.presentation.common

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseScaffold(
    title: @Composable () -> Unit = {},
    topActions: @Composable RowScope.() -> Unit = {},
    navigation: @Composable () -> Unit = {},
    fabContent: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainer,
    titleContentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    actionIconContentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = title,
                actions = topActions,
                navigationIcon = navigation,
                colors = TopAppBarDefaults.topAppBarColors().copy(
                    containerColor = containerColor,
                    titleContentColor = titleContentColor,
                    actionIconContentColor = actionIconContentColor
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
        floatingActionButton = fabContent,
        bottomBar = bottomBar
    ) { paddingValues -> content(paddingValues) }
}