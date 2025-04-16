package com.umutsaydam.deardiary.presentation.diaries

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.GenericFontFamily
import com.umutsaydam.deardiary.domain.entity.DiaryEntity

@Composable
fun DiaryListLazyRow(
    modifier: Modifier = Modifier,
    diaryEntityList: List<DiaryEntity>,
    defaultFont: GenericFontFamily,
    onClick: (DiaryEntity) -> Unit,
    onLongClick: (DiaryEntity) -> Unit
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
    ) {
        items(count = diaryEntityList.size, key = { diaryEntityList[it].diaryId }) { index ->
            DiaryListItem(
                index = index,
                diaryEntity = diaryEntityList[index],
                defaultFont = defaultFont,
                onClick = { entity -> onClick(entity) },
                onLongClick = { entity -> onLongClick(entity) }
            )
        }
    }
}