package com.umutsaydam.deardiary.presentation.diaries

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.umutsaydam.deardiary.R
import com.umutsaydam.deardiary.domain.entity.DiaryEntity
import com.umutsaydam.deardiary.presentation.Dimens.CORNER_MEDIUM
import com.umutsaydam.deardiary.presentation.common.BaseAlertDialog
import com.umutsaydam.deardiary.presentation.common.BaseScaffold
import com.umutsaydam.deardiary.presentation.common.LoadingCircular
import com.umutsaydam.deardiary.presentation.common.MainNavigationAppBar
import com.umutsaydam.deardiary.presentation.navigation.Route
import com.umutsaydam.deardiary.util.safeNavigate
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Composable
fun DiariesScreen(
    navController: NavHostController,
    diariesViewModel: DiariesViewModel = hiltViewModel()
) {
    val isLoading by diariesViewModel.isLoading.collectAsState()
    val diariesList by diariesViewModel.diariesList.collectAsState()
    val isTokenExpired by diariesViewModel.isTokenExpired.collectAsState()
    val uiMessageState by diariesViewModel.uiMessageState.collectAsState()
    val context = LocalContext.current
    var isDeleteDialogOpen by remember { mutableStateOf(false) }
    var selectedDiaryEntity: DiaryEntity? by remember { mutableStateOf(null) }

    LaunchedEffect(isTokenExpired) {
        if (isTokenExpired) {
            navController.safeNavigate(Route.Auth.route)
        }
    }

    LaunchedEffect(uiMessageState) {
        if (uiMessageState.isNotEmpty()) {
            Toast.makeText(context, uiMessageState, Toast.LENGTH_SHORT).show()
            diariesViewModel.clearUiMessageState()
        }
    }

    BaseScaffold(
        title = "Diaries",
        topActions = {
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
        fabContent = {
            AddDiaryFab { navController.safeNavigate(Route.AddDiary.route) }
        },
        bottomBar = { MainNavigationAppBar(navController) }
    ) { paddingValues ->
        if (isLoading) {
            LoadingCircular()
        }

        if (isDeleteDialogOpen) {
            BaseAlertDialog(
                icon = R.drawable.ic_delete_outline,
                contentDesc = "Delete icon",
                title = "Delete Diary",
                text = { Text("Diary is deleting. Are you sure?") },
                onDismissed = { isDeleteDialogOpen = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            selectedDiaryEntity?.let {
                                diariesViewModel.deleteDiaryById(it)
                                isDeleteDialogOpen = false
                            }
                        }
                    ) {
                        Text("Yes")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { isDeleteDialogOpen = false }
                    ) {
                        Text("Cancel")
                    }
                }
            )
        }

        DiaryListLazyRow(
            modifier = Modifier.padding(
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding()
            ),
            diaryEntityList = diariesList,
            onClick = { diaryEntity ->
                val json = Json.encodeToString(diaryEntity)
                val encodedJson = Uri.encode(json).replace("+", "%20")
                navController.navigate("ReadDiary?diaryJson=$encodedJson")
            },
            onLongClick = { diaryEntity ->
                selectedDiaryEntity = diaryEntity
                isDeleteDialogOpen = true
            }
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
