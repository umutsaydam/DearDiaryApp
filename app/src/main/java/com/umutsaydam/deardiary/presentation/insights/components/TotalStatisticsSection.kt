package com.umutsaydam.deardiary.presentation.insights.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.umutsaydam.deardiary.ui.theme.LightBlue
import com.umutsaydam.deardiary.ui.theme.SemiLightBlue

@Composable
fun TotalStatisticsSection(
    totalCountOfDiaries: String,
    contentTotalCountOfDiaries: String,
    countOfCurrentStreak: String,
    contentOfCurrentStreak: String,
    countOfLongestStreak: String,
    contentOfLongestStreak: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            StatisticsTitle(totalCountOfDiaries)
            StatisticsContent(statisticsContent = contentTotalCountOfDiaries)
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.size(100.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(
                    modifier = Modifier
                        .size(100.dp)
                ) {
                    drawCircle(
                        brush = Brush.linearGradient(
                            listOf(
                                LightBlue,
                                SemiLightBlue
                            )
                        ),
                        style = Stroke(15f),
                        radius = size.minDimension / 3,
                        center = Offset(size.width / 2, size.height / 2)
                    )

                    val width = size.width
                    val height = size.height

                    val dropWidth = width / 4f
                    val dropHeight = height / 3f
                    val offsetY = 90f

                    val path = Path().apply {
                        moveTo(width / 2f, height / 4f - offsetY)
                        cubicTo(
                            width / 2f - dropWidth / 2f,
                            height / 4f + dropHeight / 4f - offsetY,
                            width / 2f - dropWidth / 2f,
                            height / 4f + dropHeight * 3f / 4f - offsetY,
                            width / 2f,
                            height / 4f + dropHeight - offsetY
                        )
                        cubicTo(
                            width / 2f + dropWidth / 2f,
                            height / 4f + dropHeight * 3f / 4f - offsetY,
                            width / 2f + dropWidth / 2f,
                            height / 4f + dropHeight / 4f - offsetY,
                            width / 2f,
                            height / 4f - offsetY
                        )
                        close()
                    }

                    val gradient = Brush.verticalGradient(
                        colors = listOf(
                            LightBlue,
                            SemiLightBlue
                        ),
                        startY = height / 4f,
                        endY = height / 4f + dropHeight
                    )

                    drawPath(path, brush = gradient)
                    drawPath(path, color = Color.White, style = Stroke(8f))

                    val highlightOffset = Offset(
                        width / 2f - dropWidth / 8f,
                        height / 4f + dropHeight / 8f - offsetY
                    )
                    val highlightRadius = dropWidth / 6f

                    drawCircle(
                        color = Color.White,
                        center = highlightOffset,
                        radius = highlightRadius
                    )

                }
                Text(
                    text = countOfCurrentStreak
                )
            }
            StatisticsContent(
                modifier = Modifier.padding(bottom = 5.dp),
                statisticsContent = contentOfCurrentStreak
            )
        }
        Column(
            modifier = Modifier
                .weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            StatisticsTitle(countOfLongestStreak)
            StatisticsContent(statisticsContent = contentOfLongestStreak)
        }
    }
}

@Composable
fun StatisticsTitle(statisticsTitle: String) {
    Text(
        text = statisticsTitle,
        style = MaterialTheme.typography.bodyLarge.copy(
            fontWeight = FontWeight.Bold,
        ),
        color = MaterialTheme.colorScheme.outline,
        textAlign = TextAlign.Center
    )
}

@Composable
fun StatisticsContent(modifier: Modifier = Modifier, statisticsContent: String) {
    Text(
        modifier = modifier,
        text = statisticsContent,
        style = MaterialTheme.typography.bodySmall.copy(
            fontWeight = FontWeight.Normal,
        ),
        color = MaterialTheme.colorScheme.outline,
        textAlign = TextAlign.Center
    )
}