package com.umutsaydam.deardiary.presentation.addDiary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umutsaydam.deardiary.R
import com.umutsaydam.deardiary.domain.sealedStates.Resource
import com.umutsaydam.deardiary.domain.sealedStates.UiMessage
import com.umutsaydam.deardiary.domain.sealedStates.UiState
import com.umutsaydam.deardiary.domain.entity.DiaryEntity
import com.umutsaydam.deardiary.domain.useCases.IsInternetAvailableUseCase
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
    private val saveDiaryRoomUseCase: UpsertDiaryRoomUseCase,
    private val isInternetAvailableUseCase: IsInternetAvailableUseCase
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
            _uiMessageState.value = UiMessage.Error(R.string.content_can_not_empty)
            return
        }

        viewModelScope.launch {
            if(isInternetAvailableUseCase()){
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
                            _uiMessageState.value =
                                UiMessage.Success(R.string.diary_added)
                        }
                    }

                    is Resource.Error -> {
                        _uiMessageState.value = UiMessage.Error(
                            message = result.message ?: R.string.something_went_wrong,
                            statusCode = result.status
                        )
                        _addDiaryUiState.value = UiState.Idle
                    }
                }
            }else{
                _uiMessageState.value =
                    UiMessage.Error(R.string.something_went_wrong)
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