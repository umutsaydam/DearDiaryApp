package com.umutsaydam.deardiary.presentation.addDiary.diaryTemplate

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.umutsaydam.deardiary.R
import com.umutsaydam.deardiary.domain.entity.DiaryTemplateEntity
import com.umutsaydam.deardiary.presentation.common.BaseAlertDialog

@Composable
fun DiaryTemplateDialog(
    modifier: Modifier = Modifier,
    templateList: List<DiaryTemplateEntity>,
    onTemplateSelected: (DiaryTemplateEntity) -> Unit,
    onDismissed: () -> Unit
) {
    var isInfoDialogOpen by remember { mutableStateOf(false) }
    var selectedTemplateIndex by remember { mutableIntStateOf(-1) }

    if (isInfoDialogOpen && selectedTemplateIndex != -1) {
        DiaryTemplateInfoDialog(
            templateTitle = templateList[selectedTemplateIndex].templateTitle,
            templateContent = templateList[selectedTemplateIndex].templateDiaryContents,
            onDismissed = { isInfoDialogOpen = false }
        )
    }

    BaseAlertDialog(
        modifier = modifier.fillMaxHeight(0.8F),
        icon = R.drawable.ic_sticky_note_outline,
        contentDesc = stringResource(R.string.template_dairy_icon),
        title = stringResource(R.string.diary_templates),
        text = {
            DiaryTemplateListLazyColumn(
                diaryTemplateEntityList = templateList,
                onInfoSelected = { index ->
                    selectedTemplateIndex = index
                    isInfoDialogOpen = true
                },
                onTemplateSelected = { index ->
                    onTemplateSelected(templateList[index])
                }
            )
        },
        onDismissed = { onDismissed() },
    )
}

@Composable
fun DiaryTemplateInfoDialog(
    modifier: Modifier = Modifier,
    templateTitle: String,
    templateContent: String,
    onDismissed: () -> Unit
) {
    BaseAlertDialog(
        modifier = modifier.fillMaxHeight(0.4F),
        icon = R.drawable.ic_info_outline,
        contentDesc = stringResource(R.string.information_about_the_diary_template),
        title = templateTitle,
        text = { Text(templateContent) },
        onDismissed = { onDismissed() },
        confirmButton = {
            Button(
                onClick = { onDismissed() }
            ) {
                Text(stringResource(R.string.okay))
            }
        }
    )
}