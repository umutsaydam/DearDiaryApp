package com.umutsaydam.deardiary.presentation.addDiary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umutsaydam.deardiary.domain.Resource
import com.umutsaydam.deardiary.domain.UiMessage
import com.umutsaydam.deardiary.domain.UiState
import com.umutsaydam.deardiary.domain.entity.DiaryEntity
import com.umutsaydam.deardiary.domain.useCases.local.diaryRoomUseCase.UpsertDiaryRoomUseCase
import com.umutsaydam.deardiary.domain.useCases.remote.diaryServerUseCase.SaveDiaryServerUseCase
import com.umutsaydam.deardiary.util.DateFormatter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class AddDiaryViewModel @Inject constructor(
    private val saveDiaryServerUseCase: SaveDiaryServerUseCase,
    private val saveDiaryRoomUseCase: UpsertDiaryRoomUseCase
) : ViewModel() {

    private val _uiMessageState = MutableStateFlow<UiMessage?>(null)
    val uiMessageState: StateFlow<UiMessage?> = _uiMessageState

    private val _addDiaryUiState = MutableStateFlow<UiState<DiaryEntity>>(UiState.Idle)
    val addDiaryUiState: StateFlow<UiState<DiaryEntity>> = _addDiaryUiState

    private val _selectedDateMillis = MutableStateFlow(Calendar.getInstance().timeInMillis)
    val selectedDateMillis: StateFlow<Long> = _selectedDateMillis

    private val _diaryContent = MutableStateFlow("")
    val diaryContent: StateFlow<String> = _diaryContent

    private val _diaryEmotion = MutableStateFlow(-1)
    val diaryEmotion: StateFlow<Int> = _diaryEmotion

    fun saveDiaryServer() {
        if (_diaryContent.value.isBlank()) {
            _uiMessageState.value = UiMessage.Error("Content can not be empty.")
            return
        }

        viewModelScope.launch {
            _addDiaryUiState.value = UiState.Loading

            val result = saveDiaryServerUseCase(
                DiaryEntity(
                    diaryContent = diaryContent.value.trim(),
                    diaryDate = DateFormatter.formatForSelectedDateForServer(_selectedDateMillis.value),
                    diaryEmotion = _diaryEmotion.value
                )
            )

            when (result) {
                is Resource.Success -> {
                    result.data?.let {
                        saveDiaryRoomUseCase(it)
                        _addDiaryUiState.value = UiState.Success(it)
                    }
                }

                is Resource.Error -> {
                    _uiMessageState.value = UiMessage.Error(
                        message = result.message ?: "Something went wrong.",
                        statusCode = result.status
                    )
                    _addDiaryUiState.value = UiState.Idle
                }

                is Resource.Loading -> {}
            }
        }
    }

    fun clearUiMessageState() {
        _uiMessageState.value = null
    }

    fun updateSelectedDate(selectedDateMillis: Long) {
        _selectedDateMillis.value = selectedDateMillis
    }

    fun formatForSelectedDate(millis: Long): String {
        return DateFormatter.formatForSelectedDate(millis)
    }

    fun updateDiaryContent(diaryText: String) {
        _diaryContent.value = diaryText
    }

    fun updateDiaryEmotion(selectedIndex: Int) {
        _diaryEmotion.value = selectedIndex
    }
}