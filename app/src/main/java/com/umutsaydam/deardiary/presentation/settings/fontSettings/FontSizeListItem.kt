package com.umutsaydam.deardiary.presentation.settings.fontSettings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.GenericFontFamily
import com.umutsaydam.deardiary.presentation.Dimens.PaddingSmall
import com.umutsaydam.deardiary.presentation.Dimens.CornerSmall

@Composable
fun FontSizeListItem(
    modifier: Modifier = Modifier,
    textStyle: TextStyle,
    fontFamily: GenericFontFamily,
    index: Int,
    isSelected: Boolean,
    onSelected: (Int) -> Unit
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(CornerSmall))
            .fillMaxWidth()
            .background(
                if (isSelected) MaterialTheme.colorScheme.surfaceContainerLowest
                else MaterialTheme.colorScheme.surfaceContainerLow
            )
            .clickable {
                onSelected(index)
            }
            .padding(top = PaddingSmall, bottom = PaddingSmall, start = PaddingSmall)
    ) {
        Text(
            text = "Dear diary, I am so happy.",
            style = textStyle.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontFamily = fontFamily
            ),
        )
    }
}