package com.umutsaydam.deardiary.presentation.settings.fontSettings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.GenericFontFamily
import com.umutsaydam.deardiary.presentation.Dimens.SpacingSmall

@Composable
fun FontSizeListLazyColumn(
    modifier: Modifier = Modifier,
    textStyleList: List<TextStyle>,
    fontFamily: GenericFontFamily,
    selectedIndex: Int,
    onSelected: (Int) -> Unit
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(SpacingSmall)
    ) {
        items(count = textStyleList.size, key = { it }) { index ->
            FontSizeListItem(
                textStyle = textStyleList[index],
                fontFamily = fontFamily,
                index = index,
                isSelected = selectedIndex == index,
                onSelected = { styleIndex ->
                    onSelected(styleIndex) }
            )
        }
    }
}