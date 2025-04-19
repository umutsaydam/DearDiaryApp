package com.umutsaydam.deardiary.presentation.insights.components

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import com.umutsaydam.deardiary.domain.sealedStates.InsightEmotionTimeState
import com.umutsaydam.deardiary.ui.theme.SemiLightBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SingleChoiceSegmentedButtons(
    rememberScrollState: ScrollState,
    options: List<InsightEmotionTimeState>,
    selectedEmotionTimeState: InsightEmotionTimeState,
    onSelectedIndexChanged: (InsightEmotionTimeState) -> Unit
) {
    SingleChoiceSegmentedButtonRow(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState)
    ) {
        options.forEachIndexed { index, emotionState ->
            SegmentedButton(
                selected = selectedEmotionTimeState == emotionState,
                onClick = {
                    onSelectedIndexChanged(emotionState)
                },
                label = {
                    Text(
                        text = emotionState.insightContent,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                shape = SegmentedButtonDefaults.itemShape(
                    index = index,
                    count = options.size
                ),
                colors = SegmentedButtonDefaults.colors().copy(
                    activeContainerColor = SemiLightBlue,
                    activeContentColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                    activeBorderColor = MaterialTheme.colorScheme.outlineVariant,
                    inactiveContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                    inactiveContentColor = MaterialTheme.colorScheme.outline,
                    inactiveBorderColor = MaterialTheme.colorScheme.outlineVariant,
                )
            )
        }
    }
}