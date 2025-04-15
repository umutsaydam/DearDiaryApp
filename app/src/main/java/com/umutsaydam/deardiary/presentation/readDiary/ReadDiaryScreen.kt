package com.umutsaydam.deardiary.presentation.readDiary

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.changedToUp
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.umutsaydam.deardiary.R
import com.umutsaydam.deardiary.domain.entity.DiaryEntity
import com.umutsaydam.deardiary.domain.entity.emotionList
import com.umutsaydam.deardiary.domain.entity.templateList
import com.umutsaydam.deardiary.presentation.addDiary.BottomXRMenu
import com.umutsaydam.deardiary.presentation.addDiary.DairyMoodPopup
import com.umutsaydam.deardiary.presentation.addDiary.diaryTemplate.DiaryTemplateDialog
import com.umutsaydam.deardiary.presentation.common.BaseScaffold
import com.umutsaydam.deardiary.presentation.common.LoadingCircular
import com.umutsaydam.deardiary.presentation.navigation.Route
import com.umutsaydam.deardiary.util.DateFormatter
import com.umutsaydam.deardiary.util.popBackStackOrIgnore
import com.umutsaydam.deardiary.util.safeNavigate

@Composable
fun ReadDiaryScreen(
    navController: NavHostController,
    diaryEntity: DiaryEntity,
    readDiaryViewModel: ReadDiaryViewModel = hiltViewModel()
) {
    val diary by readDiaryViewModel.diary.collectAsState()
    LaunchedEffect(Unit) { readDiaryViewModel.setDiary(diaryEntity) }

    diary?.let {
        val isLoading by readDiaryViewModel.isLoading.collectAsState()
        val isTokenExpired by readDiaryViewModel.isTokenExpired.collectAsState()
        var isTemplateDialogOpen by remember { mutableStateOf(false) }
        var isMoodDialogOpen by remember { mutableStateOf(false) }
        var isLongPressingMoodButton by remember { mutableStateOf(false) }
        val uiMessageState by readDiaryViewModel.uiMessageState.collectAsState()
        val selectedIndex by readDiaryViewModel.diaryEmotion.collectAsState()
        val diaryContent by readDiaryViewModel.diaryContent.collectAsState()

        val context = LocalContext.current
        val configuration = LocalConfiguration.current
        val density = LocalDensity.current
        val screenWidthPx = with(density) { configuration.screenWidthDp.dp.toPx() }

        LaunchedEffect(isTokenExpired) {
            if (isTokenExpired) {
                navController.safeNavigate(Route.Auth.route)
            }
        }

        LaunchedEffect(uiMessageState) {
            if (uiMessageState.isNotEmpty()) {
                Toast.makeText(context, uiMessageState, Toast.LENGTH_SHORT).show()
                readDiaryViewModel.clearUiMessageState()
            }
        }

        BaseScaffold(
            title = DateFormatter.formatForUi(it.diaryDate!!),
            topActions = {
                IconButton(
                    onClick = { readDiaryViewModel.update() },
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_update_filled),
                        contentDescription = "Update the diary"
                    )
                }
            },
            navigation = {
                IconButton(
                    onClick = {
                        navController.popBackStackOrIgnore()
                    }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_arrow_back_filled),
                        contentDescription = "Back to the previous screen"
                    )
                }
            }
        ) { paddingValues ->
            if (isLoading) {
                LoadingCircular()
            }

            if (isTemplateDialogOpen) {
                DiaryTemplateDialog(
                    templateList = templateList,
                    onTemplateSelected = { selectedTemplate ->
                        readDiaryViewModel.addTemplate(selectedTemplate.templateDiaryContents)
                        isTemplateDialogOpen = false
                    },
                    onDismissed = { isTemplateDialogOpen = false }
                )
            }

            if (isMoodDialogOpen && isLongPressingMoodButton) {
                DairyMoodPopup(
                    emotionList = emotionList,
                    selectedIndex = selectedIndex!!,
                    onDismissed = {
                        isMoodDialogOpen = false
                        isLongPressingMoodButton = false
                    }
                )
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                item {
                    BasicTextField(
                        modifier = Modifier
                            .fillParentMaxSize(),
                        value = diaryContent!!,
                        onValueChange = { value -> readDiaryViewModel.updateDiaryContent(value) }
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .consumeWindowInsets(paddingValues)
                    .imePadding()
            ) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .pointerInput(isMoodDialogOpen) {
                            if (isMoodDialogOpen) {
                                awaitPointerEventScope {
                                    while (true) {
                                        val event = awaitPointerEvent()
                                        val pos = event.changes.first().position
                                        val popupWidthPx = 300.dp.toPx()
                                        val popupStartX = (screenWidthPx - popupWidthPx) / 2f
                                        val localX = pos.x - popupStartX
                                        val spacing = 4.dp.toPx()
                                        val totalSpacing = spacing * (emotionList.size - 1)
                                        val availableWidth = 300.dp.toPx() - totalSpacing
                                        val itemWidth = availableWidth / emotionList.size
                                        val fullItemWidth = itemWidth + spacing

                                        val index = (localX / fullItemWidth)
                                            .toInt()
                                            .coerceIn(0, emotionList.lastIndex)

                                        readDiaryViewModel.updateDiaryEmotion(index)

                                        if (event.changes
                                                .first()
                                                .changedToUp()
                                        ) {
                                            emotionList[index]
                                            isMoodDialogOpen = false
                                            isLongPressingMoodButton = false
                                            break
                                        }
                                    }
                                }
                            }
                        },
                    contentAlignment = Alignment.BottomCenter
                ) {
                    BottomXRMenu(
                        onTemplateDialogOpen = { isTemplateDialogOpen = true },
                        onLongPress = {
                            isMoodDialogOpen = true
                            isLongPressingMoodButton = true
                        },
                        onDismissed = {
                            isMoodDialogOpen = false
                            isLongPressingMoodButton = false
                        }
                    )
                }
            }
        }
    }
}