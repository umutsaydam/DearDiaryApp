package com.umutsaydam.deardiary.presentation.insights

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mikephil.charting.data.RadarEntry
import com.umutsaydam.deardiary.domain.InsightEmotionTimeState
import com.umutsaydam.deardiary.domain.Resource
import com.umutsaydam.deardiary.domain.UiMessage
import com.umutsaydam.deardiary.domain.UiState
import com.umutsaydam.deardiary.domain.entity.DiaryEmotionEntity
import com.umutsaydam.deardiary.domain.entity.TotalInsightsEntity
import com.umutsaydam.deardiary.domain.entity.emotionList
import com.umutsaydam.deardiary.domain.useCases.remote.insightsUseCase.GetTotalEmotionInsightsUseCase
import com.umutsaydam.deardiary.domain.useCases.remote.insightsUseCase.GetTotalInsightsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InsightsViewModel @Inject constructor(
    private val getTotalInsightsUseCase: GetTotalInsightsUseCase,
    private val getTotalEmotionInsightsUseCase: GetTotalEmotionInsightsUseCase
) : ViewModel() {

    private val _insightEmotionTimeState =
        MutableStateFlow<InsightEmotionTimeState>(InsightEmotionTimeState.AllTime)
    val insightEmotionTimeState: StateFlow<InsightEmotionTimeState> = _insightEmotionTimeState

    private val _totalInsightsUiState = MutableStateFlow<UiState<TotalInsightsEntity>>(UiState.Idle)
    val totalInsightsUiState: StateFlow<UiState<TotalInsightsEntity>> = _totalInsightsUiState

    private val _totalEmotionInsightsUiState =
        MutableStateFlow<UiState<List<DiaryEmotionEntity>>>(UiState.Idle)
    val totalEmotionInsightsUiState: StateFlow<UiState<List<DiaryEmotionEntity>>> =
        _totalEmotionInsightsUiState

    private val _uiMessageState = MutableStateFlow<UiMessage?>(null)
    val uiMessageState: StateFlow<UiMessage?> = _uiMessageState

    init {
        getTotalInsights()
        getTotalEmotionInsights()
    }

    private fun getTotalInsights() {
        viewModelScope.launch {
            _totalInsightsUiState.value = UiState.Loading

            when (val result = getTotalInsightsUseCase()) {
                is Resource.Success -> {
                    result.data?.let {
                        _totalInsightsUiState.value = UiState.Success(it)
                    }
                }

                is Resource.Error -> {
                    _uiMessageState.value =
                        UiMessage.Error(result.message ?: "Something went wrong.")
                    _totalInsightsUiState.value = UiState.Idle
                }

                is Resource.Loading -> {}
            }

        }
    }

    private fun getTotalEmotionInsights() {
        viewModelScope.launch {
            _totalEmotionInsightsUiState.value = UiState.Loading

            when (val result =
                getTotalEmotionInsightsUseCase(_insightEmotionTimeState.value.insightParam)) {
                is Resource.Success -> {
                    result.data?.let {
                        _totalEmotionInsightsUiState.value = UiState.Success(it)
                    }
                }

                is Resource.Error -> {
                    _uiMessageState.value =
                        UiMessage.Error(result.message ?: "Something went wrong.")
                    _totalEmotionInsightsUiState.value = UiState.Idle
                }

                is Resource.Loading -> {}
            }
        }
    }

    fun getRadarEntry(): MutableList<RadarEntry> {
        val totalEmotions = (_totalEmotionInsightsUiState.value as UiState.Success).data!!
        val entries: MutableList<RadarEntry> = mutableListOf()
        emotionList.forEach { emotion ->
            val matched = totalEmotions.find { it.emotionId == emotion.emotionId }
            val count = matched?.emotionCount ?: 0
            entries.add(RadarEntry(count.toFloat()))
        }
        return entries
    }

    fun clearUiMessageState() {
        _uiMessageState.value = null
    }

    fun updateTimeRangeAndRecall(newEmotionTimeState: InsightEmotionTimeState) {
        if (_insightEmotionTimeState.value != newEmotionTimeState) {
            _insightEmotionTimeState.value = newEmotionTimeState
            getTotalEmotionInsights()
        }
    }
}