package com.umutsaydam.deardiary.presentation.readDiary

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.umutsaydam.deardiary.R
import com.umutsaydam.deardiary.domain.entity.DiaryEntity
import com.umutsaydam.deardiary.presentation.common.BaseScaffold
import com.umutsaydam.deardiary.presentation.common.BottomXRMenuWithGesture
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
    val defaultFont = readDiaryViewModel.defaultFont.collectAsState().value
    val defaultSize = readDiaryViewModel.defaultSize.collectAsState().value
    LaunchedEffect(Unit) { readDiaryViewModel.setDiary(diaryEntity) }

    if (diary != null && defaultFont != null && defaultSize != null) {
        val isLoading by readDiaryViewModel.isLoading.collectAsState()
        val isTokenExpired by readDiaryViewModel.isTokenExpired.collectAsState()
        val uiMessageState by readDiaryViewModel.uiMessageState.collectAsState()
        val selectedIndex by readDiaryViewModel.diaryEmotion.collectAsState()
        val diaryContent by readDiaryViewModel.diaryContent.collectAsState()
        val context = LocalContext.current

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
            title = DateFormatter.formatForUi(diary!!.diaryDate!!),
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
                        onValueChange = { value -> readDiaryViewModel.updateDiaryContent(value) },
                        textStyle = fontSizeLabelFromTextStyle(defaultSize).copy(
                            fontFamily = defaultFont
                        )
                    )
                }
            }

            BottomXRMenuWithGesture(
                selectedIndex = selectedIndex!!,
                paddingValues = paddingValues,
                onMoodSelected = { mood ->
                    readDiaryViewModel.updateDiaryEmotion(mood)
                },
                onTemplateSelected = { templateDiaryContents ->
                    readDiaryViewModel.addTemplate(templateDiaryContents)
                }
            )
        }
    }
}

@Composable
fun fontSizeLabelFromTextStyle(fontSizeId: Int): TextStyle {
    return when (fontSizeId) {
        0 -> MaterialTheme.typography.headlineMedium
        1 -> MaterialTheme.typography.headlineSmall
        2 -> MaterialTheme.typography.bodyLarge
        3 -> MaterialTheme.typography.bodyMedium
        else -> MaterialTheme.typography.bodyMedium
    }
}