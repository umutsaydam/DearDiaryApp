package com.umutsaydam.deardiary.presentation.addDiary.diaryTemplate

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.umutsaydam.deardiary.domain.entity.DiaryTemplateEntity
import com.umutsaydam.deardiary.presentation.Dimens.SpacingSmall

@Composable
fun DiaryTemplateListLazyColumn(
    modifier: Modifier = Modifier,
    diaryTemplateEntityList: List<DiaryTemplateEntity>,
    onTemplateSelected: (Int) -> Unit,
    onInfoSelected: (Int) -> Unit
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(SpacingSmall)
    ) {
        items(count = diaryTemplateEntityList.size, key = { it }) { index ->
            DiaryTemplateListItem(
                index = index,
                diaryTemplateEntity = diaryTemplateEntityList[index],
                onTemplateSelected = { templateIndex ->
                    onTemplateSelected(templateIndex)
                },
                onInfoSelected = { templateIndex ->
                    onInfoSelected(templateIndex)
                }
            )
        }
    }
}