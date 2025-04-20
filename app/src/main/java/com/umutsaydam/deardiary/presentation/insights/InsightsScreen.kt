package com.umutsaydam.deardiary.presentation.insights

import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.github.mikephil.charting.charts.RadarChart
import com.github.mikephil.charting.data.RadarData
import com.github.mikephil.charting.data.RadarDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.umutsaydam.deardiary.R
import com.umutsaydam.deardiary.domain.sealedStates.InsightEmotionTimeState
import com.umutsaydam.deardiary.domain.sealedStates.UiMessage
import com.umutsaydam.deardiary.domain.sealedStates.UiState
import com.umutsaydam.deardiary.domain.entity.TotalInsightsEntity
import com.umutsaydam.deardiary.domain.entity.emotionList
import com.umutsaydam.deardiary.domain.sealedStates.insightEmotionList
import com.umutsaydam.deardiary.presentation.Dimens.PaddingSmall
import com.umutsaydam.deardiary.presentation.Dimens.PaddingLarge
import com.umutsaydam.deardiary.presentation.Dimens.CornerMedium
import com.umutsaydam.deardiary.presentation.common.BaseScaffold
import com.umutsaydam.deardiary.presentation.common.LoadingCircular
import com.umutsaydam.deardiary.presentation.common.MainNavigationAppBar
import com.umutsaydam.deardiary.presentation.insights.components.SingleChoiceSegmentedButtons
import com.umutsaydam.deardiary.presentation.insights.components.TotalStatisticsSection
import com.umutsaydam.deardiary.presentation.navigation.Route
import com.umutsaydam.deardiary.ui.theme.LightBlue
import com.umutsaydam.deardiary.ui.theme.SemiLightBlue
import com.umutsaydam.deardiary.util.safeNavigateWithClearingBackStack

@Composable
fun InsightsScreen(
    navController: NavHostController,
    insightsViewModel: InsightsViewModel = hiltViewModel()
) {
    val insightEmotionTimeState by insightsViewModel.insightEmotionTimeState.collectAsState()
    val totalInsightsUiState by insightsViewModel.totalInsightsUiState.collectAsState()
    val totalEmotionInsightsUiState by insightsViewModel.totalEmotionInsightsUiState.collectAsState()
    val uiMessageState by insightsViewModel.uiMessageState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(uiMessageState) {
        when (val state = uiMessageState) {
            is UiMessage.Success -> {
                Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
                insightsViewModel.clearUiMessageState()
            }

            is UiMessage.Error -> {
                Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
                if (state.statusCode != null && state.statusCode == 401) {
                    navController.safeNavigateWithClearingBackStack(Route.Auth.route)
                }
                insightsViewModel.clearUiMessageState()
            }

            else -> {}
        }
    }

    BaseScaffold(
        title = { Text(stringResource(R.string.insights)) },
        bottomBar = { MainNavigationAppBar(navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier
                    .padding(PaddingLarge)
                    .shadow(8.dp, RoundedCornerShape(CornerMedium))
                    .clip(RoundedCornerShape(CornerMedium))
                    .background(MaterialTheme.colorScheme.surfaceContainerLowest)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if (totalInsightsUiState is UiState.Loading) {
                    LoadingCircular()
                } else if (totalInsightsUiState is UiState.Success) {
                    val totalInsights =
                        (totalInsightsUiState as UiState.Success<TotalInsightsEntity>).data!!
                    TotalStatisticsSection(
                        totalCountOfDiaries = totalInsights.totalDiaries.toString(),
                        contentTotalCountOfDiaries = stringResource(R.string.total_diaries),
                        countOfCurrentStreak = totalInsights.currentStreak.toString(),
                        contentOfCurrentStreak = stringResource(R.string.current_streak),
                        countOfLongestStreak = totalInsights.longestStreak.toString(),
                        contentOfLongestStreak = stringResource(R.string.longest_streak)
                    )
                }
            }

            if (totalEmotionInsightsUiState is UiState.Loading) {
                LoadingCircular()
            } else if (totalEmotionInsightsUiState is UiState.Success) {
                RadarChartWithSelectedButtons(
                    insightsViewModel = insightsViewModel,
                    insightEmotionTimeState = insightEmotionTimeState
                )
            }
        }
    }
}

@Composable
fun RadarChartWithSelectedButtons(
    insightsViewModel: InsightsViewModel,
    insightEmotionTimeState: InsightEmotionTimeState
) {
    SingleChoiceSegmentedButtons(
        rememberScrollState = rememberScrollState(),
        options = insightEmotionList,
        selectedEmotionTimeState = insightEmotionTimeState,
        onSelectedIndexChanged = { newEmotionTimeState ->
            insightsViewModel.updateTimeRangeAndRecall(newEmotionTimeState)
        }
    )

    Column(
        modifier = Modifier
            .padding(PaddingLarge)
            .shadow(8.dp, RoundedCornerShape(CornerMedium))
            .clip(RoundedCornerShape(CornerMedium))
            .background(MaterialTheme.colorScheme.surfaceContainerLowest)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        EmotionsRadarChart(
            insightsViewModel = insightsViewModel,
            insightEmotionTimeState = insightEmotionTimeState
        )
    }
}


@Composable
fun EmotionsRadarChart(
    modifier: Modifier = Modifier,
    insightsViewModel: InsightsViewModel,
    insightEmotionTimeState: InsightEmotionTimeState
) {
    val context = LocalContext.current
    AndroidView(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surfaceContainerLowest)
            .fillMaxWidth()
            .height(300.dp)
            .padding(PaddingSmall),
        factory = {
            RadarChart(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                description.isEnabled = true
                setBackgroundColor(Color.White.toArgb())
                legend.isEnabled = false
                description.text = ""
                isRotationEnabled = false

                val radarDataSet = RadarDataSet(
                    insightsViewModel.getRadarEntry(),
                    insightEmotionTimeState.insightContent
                ).apply {
                    color = SemiLightBlue.toArgb()
                    fillColor = LightBlue.toArgb()
                    setDrawFilled(true)
                    lineWidth = 2f
                    isDrawHighlightCircleEnabled = true
                    setDrawHighlightIndicators(false)
                }

                data = RadarData(radarDataSet)

                xAxis.apply {
                    labelCount = emotionList.size
                    valueFormatter = object : ValueFormatter() {
                        override fun getFormattedValue(value: Float): String {
                            return emotionList.getOrNull(value.toInt())?.emotionContent
                                ?: ""
                        }
                    }
                }

                yAxis.apply {
                    axisMinimum = 0f
                    setLabelCount(emotionList.size, true)
                    textSize = 12f
                    setDrawLabels(true)
                    setDrawTopYLabelEntry(true)
                }

                invalidate()
            }
        },
        update = { chart ->
            val newEntries = insightsViewModel.getRadarEntry()
            val updatedDataSet =
                RadarDataSet(
                    newEntries,
                    insightEmotionTimeState.insightContent
                ).apply {
                    color = SemiLightBlue.toArgb()
                    fillColor = LightBlue.toArgb()
                    setDrawFilled(true)
                    lineWidth = 2f
                    isDrawHighlightCircleEnabled = true
                    setDrawHighlightIndicators(false)
                }

            chart.data = RadarData(updatedDataSet)
            chart.invalidate()
        }
    )
}