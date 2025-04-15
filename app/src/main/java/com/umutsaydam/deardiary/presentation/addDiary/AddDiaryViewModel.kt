package com.umutsaydam.deardiary.presentation.addDiary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umutsaydam.deardiary.domain.Resource
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

    private val _uiMessageState = MutableStateFlow("")
    val uiMessageState: StateFlow<String> = _uiMessageState

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isTokenExpired = MutableStateFlow(false)
    val isTokenExpired: StateFlow<Boolean> = _isTokenExpired

    private val _selectedDateMillis = MutableStateFlow(Calendar.getInstance().timeInMillis)
    val selectedDateMillis: StateFlow<Long> = _selectedDateMillis

    private val _diaryContent = MutableStateFlow("")
    val diaryContent: StateFlow<String> = _diaryContent

    private val _diaryEmotion = MutableStateFlow(-1)
    val diaryEmotion: StateFlow<Int> = _diaryEmotion

    fun saveDiaryServer() {
        if (_diaryContent.value.isNotEmpty()) {
            _isLoading.value = true
            viewModelScope.launch {
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
                        }
                        _isLoading.value = false
                    }

                    is Resource.Error -> {
                        val message = result.message
                        val statusCode = result.status
                        if (message != null) {
                            _uiMessageState.value = message
                        }
                        if (statusCode != null && statusCode == 401) {
                            _isTokenExpired.value = true
                        }
                        _isLoading.value = false
                    }

                    is Resource.Loading -> {}
                }
            }
        } else {
            _uiMessageState.value = "Content can not be empty."
        }
    }

    fun clearUiMessageState() {
        _uiMessageState.value = ""
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