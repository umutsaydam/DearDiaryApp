package com.umutsaydam.deardiary.presentation.diary

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.umutsaydam.deardiary.domain.DiaryEntity

@Composable
fun DiaryListLazyRow(
    modifier: Modifier = Modifier,
    diaryEntityList: List<DiaryEntity>
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
    ) {
        items(count = diaryEntityList.size, key = { it }) { index ->
            DiaryListItem(index = index, diaryEntity = diaryEntityList[index])
        }
    }
}