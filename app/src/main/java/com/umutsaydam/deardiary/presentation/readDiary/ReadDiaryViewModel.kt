package com.umutsaydam.deardiary.presentation.readDiary

import androidx.compose.ui.text.font.GenericFontFamily
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umutsaydam.deardiary.R
import com.umutsaydam.deardiary.domain.sealedStates.Resource
import com.umutsaydam.deardiary.domain.sealedStates.UiMessage
import com.umutsaydam.deardiary.domain.sealedStates.UiState
import com.umutsaydam.deardiary.domain.entity.DiaryEntity
import com.umutsaydam.deardiary.domain.entity.FontFamilySealed
import com.umutsaydam.deardiary.domain.entity.FontSizeSealed
import com.umutsaydam.deardiary.domain.useCases.local.diaryRoomUseCase.UpsertDiaryRoomUseCase
import com.umutsaydam.deardiary.domain.useCases.local.fontFamilyAndSizeUseCase.GetFontFamilyUseCase
import com.umutsaydam.deardiary.domain.useCases.local.fontFamilyAndSizeUseCase.GetFontSizeUseCase
import com.umutsaydam.deardiary.domain.useCases.remote.diaryServerUseCase.UpdateDiaryServerUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReadDiaryViewModel @Inject constructor(
    private val getFontFamilyUseCase: GetFontFamilyUseCase,
    private val getFontSizeUseCase: GetFontSizeUseCase,
    private val updateDiaryServerUseCase: UpdateDiaryServerUseCase,
    private val upsertDiaryRoomUseCase: UpsertDiaryRoomUseCase
) : ViewModel() {

    private val _defaultFont = MutableStateFlow<GenericFontFamily?>(null)
    val defaultFont: StateFlow<GenericFontFamily?> = _defaultFont

    private val _defaultSize = MutableStateFlow<Int?>(null)
    val defaultSize: StateFlow<Int?> = _defaultSize

    private val _diaryEmotion = MutableStateFlow<Int?>(null)
    val diaryEmotion: StateFlow<Int?> = _diaryEmotion

    private val _diaryContent = MutableStateFlow<String?>(null)
    val diaryContent: StateFlow<String?> = _diaryContent

    private val _uiMessageState = MutableStateFlow<UiMessage?>(null)
    val uiMessageState: StateFlow<UiMessage?> = _uiMessageState

    private val _readDiaryUiState = MutableStateFlow<UiState<DiaryEntity>>(UiState.Idle)
    val readDiaryUiState: StateFlow<UiState<DiaryEntity>> = _readDiaryUiState

    init {
        getDefaultFont()
        getDefaultSize()
    }

    fun setDiary(diary: DiaryEntity) {
        _readDiaryUiState.value = UiState.Success(diary)
        _diaryEmotion.value = diary.diaryEmotion
        _diaryContent.value = diary.diaryContent
    }

    fun update() {
        val currDiary = (_readDiaryUiState.value as? UiState.Success)?.data?.copy()
        currDiary?.let {
            if (_diaryContent.value == it.diaryContent && _diaryEmotion.value == it.diaryEmotion) {
                _uiMessageState.value = UiMessage.Error(R.string.diary_already_update)
                return
            }

            if (_diaryContent.value.isNullOrEmpty()) {
                _uiMessageState.value = UiMessage.Error(R.string.content_can_not_empty)
                return
            }

            viewModelScope.launch {
                _readDiaryUiState.value = UiState.Loading
                it.diaryContent = _diaryContent.value!!
                it.diaryEmotion = _diaryEmotion.value!!
                when (val result = updateDiaryServerUseCase(it)) {
                    is Resource.Success -> {
                        result.data?.let { newDiary ->
                            setDiary(newDiary)
                            upsertDiaryRoomUseCase(newDiary)
                            _uiMessageState.value =
                                UiMessage.Success(R.string.diary_updated_successfully)
                        }
                    }

                    is Resource.Error -> {
                        _uiMessageState.value = UiMessage.Error(
                            message = result.message ?: R.string.something_went_wrong,
                            statusCode = result.status
                        )
                        _readDiaryUiState.value = UiState.Idle
                    }
                }
            }
        }
    }

    private fun getDefaultFont() {
        viewModelScope.launch {
            _defaultFont.value =
                FontFamilySealed.fromLabel(getFontFamilyUseCase().first()).fontFamily
        }
    }

    private fun getDefaultSize() {
        viewModelScope.launch {
            _defaultSize.value = FontSizeSealed.fromLabel(getFontSizeUseCase().first()).id
        }
    }

    fun clearUiMessageState() {
        _uiMessageState.value = null
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