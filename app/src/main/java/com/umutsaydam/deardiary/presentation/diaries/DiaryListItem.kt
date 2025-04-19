package com.umutsaydam.deardiary.presentation.diaries

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.GenericFontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.umutsaydam.deardiary.domain.entity.DiaryEntity
import com.umutsaydam.deardiary.domain.entity.emotionList
import com.umutsaydam.deardiary.presentation.Dimens.PaddingXSmall
import com.umutsaydam.deardiary.presentation.Dimens.PaddingLarge
import com.umutsaydam.deardiary.presentation.Dimens.SizeAvatarSmall
import com.umutsaydam.deardiary.presentation.Dimens.StrokeMedium
import com.umutsaydam.deardiary.presentation.Dimens.StrokeThin
import com.umutsaydam.deardiary.util.DateFormatter

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DiaryListItem(
    modifier: Modifier = Modifier,
    index: Int,
    diaryEntity: DiaryEntity,
    defaultFont: GenericFontFamily,
    onClick: (DiaryEntity) -> Unit,
    onLongClick: (DiaryEntity) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(220.dp)
            .combinedClickable(
                onClick = { onClick(diaryEntity) },
                onLongClick = {
                    onLongClick(diaryEntity)
                }
            ),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.Start
    ) {
        DrawTimeLine(index = index)
        TimeLineContent(diaryEntity, defaultFont)
    }
}

@Composable
fun DrawTimeLine(
    modifier: Modifier = Modifier,
    index: Int,
    color: Color = MaterialTheme.colorScheme.surfaceVariant
) {
    Canvas(
        modifier = modifier
            .width(40.dp)
            .height(250.dp)
    ) {
        val centerX = size.width / 2
        val topCircleY = 20f
        val radius = 7.dp.toPx()

        if (index == 0) {
            drawCircle(
                center = Offset(x = centerX, y = topCircleY),
                radius = radius,
                color = color,
                style = Stroke(width = StrokeThin.toPx())
            )
        } else {
            drawCircle(
                center = Offset(x = centerX, y = topCircleY),
                radius = radius,
                color = color
            )
        }

        drawLine(
            start = Offset(x = centerX, y = topCircleY + radius),
            end = Offset(x = centerX, y = size.height),
            color = color,
            strokeWidth = StrokeMedium.toPx()
        )
    }
}

@Composable
fun TimeLineContent(
    diaryEntity: DiaryEntity,
    defaultFont: GenericFontFamily,
) {
    Column(
        modifier = Modifier.padding(end = PaddingXSmall)
    ) {
        Text(
            text = DateFormatter.formatForUi(diaryEntity.diaryDate!!),
            style = MaterialTheme.typography.headlineSmall.copy(
                fontFamily = defaultFont
            ),
            color = MaterialTheme.colorScheme.onSurface
        )

        Text(
            text = diaryEntity.diaryContent,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontFamily = defaultFont
            ),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 5,
            textAlign = TextAlign.Justify,
            overflow = TextOverflow.Ellipsis
        )

        ShowMoodByIndex(
            modifier = Modifier.padding(top = PaddingLarge),
            diaryEmotion = diaryEntity.diaryEmotion!!
        )
    }
}

@Composable
fun ShowMoodByIndex(modifier: Modifier = Modifier, diaryEmotion: Int) {
    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(emotionList[diaryEmotion].emotionSource))

    LottieAnimation(
        modifier = modifier
            .size(SizeAvatarSmall),
        composition = composition,
        isPlaying = false
    )
}
