package com.umutsaydam.deardiary.presentation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.umutsaydam.deardiary.domain.DiaryEntity

@Composable
fun DiaryListItem(
    modifier: Modifier = Modifier,
    index: Int,
    diaryEntity: DiaryEntity
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(220.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.Start
    ) {
        DrawTimeLine(index = index)
        TimeLineContent(diaryEntity)
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
                style = Stroke(width = 2.dp.toPx())
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
            strokeWidth = 5.dp.toPx()
        )
    }
}

@Composable
fun TimeLineContent(diaryEntity: DiaryEntity) {
    Column(
        modifier = Modifier.padding(end = 5.dp)
    ) {
        Text(
            text = "10 April 2025",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface
        )

        Text(
            text = diaryEntity.diaryContent,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 5,
            textAlign = TextAlign.Justify,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            modifier = Modifier.padding(top = 15.dp),
            text = "\uD83D\uDE02",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}