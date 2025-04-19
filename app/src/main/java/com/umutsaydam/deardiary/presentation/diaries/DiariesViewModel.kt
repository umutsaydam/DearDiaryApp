package com.umutsaydam.deardiary.presentation.diaries

import androidx.compose.ui.text.font.GenericFontFamily
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umutsaydam.deardiary.domain.Resource
import com.umutsaydam.deardiary.domain.UiMessage
import com.umutsaydam.deardiary.domain.UiState
import com.umutsaydam.deardiary.domain.entity.DiaryEntity
import com.umutsaydam.deardiary.domain.entity.FontFamilySealed
import com.umutsaydam.deardiary.domain.useCases.local.diaryRoomUseCase.DeleteDiaryRoomUseCase
import com.umutsaydam.deardiary.domain.useCases.remote.diaryServerUseCase.GetDiariesServerUseCase
import com.umutsaydam.deardiary.domain.useCases.local.diaryRoomUseCase.GetDiariesRoomUseCase
import com.umutsaydam.deardiary.domain.useCases.local.diaryRoomUseCase.InsertAllDiariesRoomUseCase
import com.umutsaydam.deardiary.domain.useCases.local.fontFamilyAndSizeUseCase.GetFontFamilyUseCase
import com.umutsaydam.deardiary.domain.useCases.remote.diaryServerUseCase.DeleteDiaryServerUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiariesViewModel @Inject constructor(
    private val getFontFamilyUseCase: GetFontFamilyUseCase,
    private val getDiariesRoomUseCase: GetDiariesRoomUseCase,
    private val getDiariesServerUseCase: GetDiariesServerUseCase,
    private val insertAllDiariesRoomUseCase: InsertAllDiariesRoomUseCase,
    private val deleteDiaryServerUseCase: DeleteDiaryServerUseCase,
    private val deleteDiariesRoomUseCase: DeleteDiaryRoomUseCase
) : ViewModel() {

    private val _defaultFont = MutableStateFlow<GenericFontFamily?>(null)
    val defaultFont: StateFlow<GenericFontFamily?> = _defaultFont

    private val _diariesUiState = MutableStateFlow<UiState<List<DiaryEntity>>>(UiState.Idle)
    val diariesUiState: StateFlow<UiState<List<DiaryEntity>>> = _diariesUiState

    private val _uiMessageState = MutableStateFlow<UiMessage?>(null)
    val uiMessageState: StateFlow<UiMessage?> = _uiMessageState

    init {
        getDefaultFont()
        getDiariesFromRoom()
    }

    private fun getDiariesFromRoom() {
        viewModelScope.launch {
            _diariesUiState.value = UiState.Loading
            getDiariesRoomUseCase().collect { diaries ->
                _diariesUiState.value = UiState.Success(diaries)
                getDiariesFromServer()
            }
        }
    }

    private fun getDiariesFromServer() {
        viewModelScope.launch {
            when (val result = getDiariesServerUseCase()) {
                is Resource.Success -> {
                    result.data?.let { diariesFromServer ->
                        addNonDiaries(diariesFromServer)
                    }
                }

                is Resource.Error -> {
                    _uiMessageState.value = UiMessage.Error(
                        message = result.message ?: "Something went wrong.",
                        statusCode = result.status
                    )
                }

                is Resource.Loading -> {}
            }
        }
    }

    private fun addNonDiaries(diariesFromServer: List<DiaryEntity>) {
        val currentData = (_diariesUiState.value as UiState.Success).data ?: emptyList()
        val localIds = currentData.map { it.diaryId }.toSet()
        val newDiaries: List<DiaryEntity> = diariesFromServer.filterNot { diary ->
            diary.diaryId in localIds
        }
        if (newDiaries.isNotEmpty()) {
            viewModelScope.launch {
                insertAllDiariesRoomUseCase(newDiaries)
            }
        }
    }

    fun deleteDiaryById(selectedDiaryEntity: DiaryEntity) {
        viewModelScope.launch {
            deleteDiariesRoomUseCase(selectedDiaryEntity)
            if (deleteDiaryServerUseCase(selectedDiaryEntity.diaryId) is Resource.Success) {
                _uiMessageState.value = UiMessage.Success("Deleted successfully.")
            } else {
                _uiMessageState.value = UiMessage.Error("Something went wrong.")
            }
        }
    }

    private fun getDefaultFont() {
        viewModelScope.launch {
            _defaultFont.value =
                FontFamilySealed.fromLabel(getFontFamilyUseCase().first()).fontFamily
        }
    }

    fun clearUiMessageState() {
        _uiMessageState.value = null
    }
}