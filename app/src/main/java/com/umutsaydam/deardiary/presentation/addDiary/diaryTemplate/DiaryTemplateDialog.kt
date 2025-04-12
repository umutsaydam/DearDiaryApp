package com.umutsaydam.deardiary.presentation.addDiary.diaryTemplate

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.umutsaydam.deardiary.R
import com.umutsaydam.deardiary.domain.DiaryTemplateEntity

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

    AlertDialog(
        modifier = modifier.fillMaxHeight(0.8F),
        icon = {
            Icon(
                painter = painterResource(R.drawable.ic_sticky_note_outline),
                contentDescription = "Desc"
            )
        },
        title = {
            Text(
                "Diary Templates"
            )
        },
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
        onDismissRequest = { onDismissed() },
        confirmButton = {},
        dismissButton = {},
        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        iconContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        titleContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        shape = AlertDialogDefaults.shape
    )
}

@Composable
fun DiaryTemplateInfoDialog(
    modifier: Modifier = Modifier,
    templateTitle: String,
    templateContent: String,
    onDismissed: () -> Unit
) {
    AlertDialog(
        modifier = modifier.fillMaxHeight(0.4F),
        icon = {
            Icon(
                painter = painterResource(R.drawable.ic_info_outline),
                contentDescription = "Information about the diary template"
            )
        },
        title = { Text(templateTitle) },
        text = { Text(templateContent) },
        onDismissRequest = { onDismissed() },
        confirmButton = {
            Button(
                onClick = { onDismissed() }
            ) {
                Text(
                    "Okay"
                )
            }
        },
        dismissButton = {},
        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        iconContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        titleContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        shape = AlertDialogDefaults.shape
    )
}