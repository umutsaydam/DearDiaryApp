package com.umutsaydam.deardiary.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import com.umutsaydam.deardiary.R
import com.umutsaydam.deardiary.domain.DiaryEntity
import com.umutsaydam.deardiary.presentation.Dimens.CORNER_MEDIUM
import java.util.Date
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiariesScreen(navController: NavHostController) {
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
        floatingActionButton = {
            AddDiaryFab {

            }
        },
        bottomBar = {
            MainNavigationAppBar(navController)
        }
    ) { paddingValues ->
        val date = Date()
        val list = listOf(
            DiaryEntity(
                UUID.randomUUID(),
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus id sollicitudin leo. Sed eget justo id sapien accumsan mattis quis nec sapien. Suspendisse commodo sed arcu non volutpat. Integer nulla justo, varius sit amet justo eget, varius molestie purus. Duis in imperdiet diam. Donec eget velit est. Fusce ipsum lacus, semper eget eros vitae, dapibus suscipit lectus. Vestibulum tempus suscipit tellus et elementum. Pellentesque faucibus purus mauris, at dignissim enim aliquet vel.",
                date,
                0
            ),
            DiaryEntity(
                UUID.randomUUID(),
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus id sollicitudin leo. Sed eget justo id sapien accumsan mattis quis nec sapien. Suspendisse commodo sed arcu non volutpat. Integer nulla justo, varius sit amet justo eget, varius molestie purus. Duis in imperdiet diam. Donec eget velit est. Fusce ipsum lacus, semper eget eros vitae, dapibus suscipit lectus. Vestibulum tempus suscipit tellus et elementum. Pellentesque faucibus purus mauris, at dignissim enim aliquet vel.",
                date,
                1
            ),
            DiaryEntity(
                UUID.randomUUID(),
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus id sollicitudin leo. Sed eget justo id sapien accumsan mattis quis nec sapien. Suspendisse commodo sed arcu non volutpat. Integer nulla justo, varius sit amet justo eget, varius molestie purus. Duis in imperdiet diam. Donec eget velit est. Fusce ipsum lacus, semper eget eros vitae, dapibus suscipit lectus. Vestibulum tempus suscipit tellus et elementum. Pellentesque faucibus purus mauris, at dignissim enim aliquet vel.",
                date,
                2
            ),
            DiaryEntity(UUID.randomUUID(), "Content3", date, 3),
            DiaryEntity(UUID.randomUUID(), "Content4", date, 4),
            DiaryEntity(UUID.randomUUID(), "Content5", date, 5),
            DiaryEntity(UUID.randomUUID(), "Content6", date, 6),
            DiaryEntity(UUID.randomUUID(), "Content7", date, 7),
            DiaryEntity(UUID.randomUUID(), "Content8", date, 8)
        )

        DiaryListLazyRow(
            modifier = Modifier.padding(
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding()
            ),
            diaryEntityList = list
        )

    }
}

@Composable
fun AddDiaryFab(onClick: () -> Unit) {
    FloatingActionButton(
        onClick = {
            onClick()
        },
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        shape = RoundedCornerShape(CORNER_MEDIUM)
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_edit_filled),
            contentDescription = "Add a diary"
        )
    }
}
