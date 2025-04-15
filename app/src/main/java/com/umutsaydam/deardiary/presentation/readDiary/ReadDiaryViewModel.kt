package com.umutsaydam.deardiary.presentation.readDiary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umutsaydam.deardiary.domain.Resource
import com.umutsaydam.deardiary.domain.entity.DiaryEntity
import com.umutsaydam.deardiary.domain.useCases.local.diaryRoomUseCase.UpsertDiaryRoomUseCase
import com.umutsaydam.deardiary.domain.useCases.remote.diaryServerUseCase.UpdateDiaryServerUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReadDiaryViewModel @Inject constructor(
    private val updateDiaryServerUseCase: UpdateDiaryServerUseCase,
    private val upsertDiaryRoomUseCase: UpsertDiaryRoomUseCase
) : ViewModel() {

    private val _diary = MutableStateFlow<DiaryEntity?>(null)
    val diary: StateFlow<DiaryEntity?> = _diary

    private val _diaryEmotion = MutableStateFlow<Int?>(null)
    val diaryEmotion: StateFlow<Int?> = _diaryEmotion

    private val _diaryContent = MutableStateFlow<String?>(null)
    val diaryContent: StateFlow<String?> = _diaryContent

    private val _uiMessageState = MutableStateFlow("")
    val uiMessageState: StateFlow<String> = _uiMessageState

    private val _isTokenExpired = MutableStateFlow(false)
    val isTokenExpired: StateFlow<Boolean> = _isTokenExpired

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun setDiary(diary: DiaryEntity) {
        _diary.value = diary
        _diaryEmotion.value = diary.diaryEmotion
        _diaryContent.value = diary.diaryContent
    }

    fun update() {
        if (_diaryContent.value != diary.value!!.diaryContent || _diaryEmotion.value != _diary.value!!.diaryEmotion) {
            _isLoading.value = true
            viewModelScope.launch {
                _diary.value!!.diaryContent = _diaryContent.value!!
                _diary.value!!.diaryEmotion = _diaryEmotion.value!!
                when (val result = updateDiaryServerUseCase(_diary.value!!)) {
                    is Resource.Success -> {
                        result.data?.let {
                            setDiary(it)
                            upsertDiaryRoomUseCase(it)
                            _uiMessageState.value = "Diary updated successfully."
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
            _uiMessageState.value = "Your diary already up to date."
        }
    }

    fun clearUiMessageState() {
        _uiMessageState.value = ""
    }

    fun updateDiaryEmotion(index: Int) {
        _diaryEmotion.value = index
    }

    fun updateDiaryContent(value: String) {
        _diaryContent.value = value
    }

    fun addTemplate(selectedTemplate: String) {
        _diaryContent.value = selectedTemplate + "\n" + _diaryContent.value
    }
}