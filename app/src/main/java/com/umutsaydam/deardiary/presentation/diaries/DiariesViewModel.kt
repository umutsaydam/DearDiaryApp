package com.umutsaydam.deardiary.presentation.diaries

import androidx.compose.ui.text.font.GenericFontFamily
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umutsaydam.deardiary.R
import com.umutsaydam.deardiary.domain.sealedStates.Resource
import com.umutsaydam.deardiary.domain.sealedStates.UiMessage
import com.umutsaydam.deardiary.domain.sealedStates.UiState
import com.umutsaydam.deardiary.domain.entity.DiaryEntity
import com.umutsaydam.deardiary.domain.entity.FontFamilySealed
import com.umutsaydam.deardiary.domain.useCases.IsInternetAvailableUseCase
import com.umutsaydam.deardiary.domain.useCases.local.diaryRoomUseCase.DeleteDiaryRoomUseCase
import com.umutsaydam.deardiary.domain.useCases.remote.diaryServerUseCase.GetDiariesServerUseCase
import com.umutsaydam.deardiary.domain.useCases.local.diaryRoomUseCase.GetDiariesRoomUseCase
import com.umutsaydam.deardiary.domain.useCases.local.diaryRoomUseCase.InsertAllDiariesRoomUseCase
import com.umutsaydam.deardiary.domain.useCases.local.diaryRoomUseCase.SearchDiaryRoomUseCase
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
    private val deleteDiariesRoomUseCase: DeleteDiaryRoomUseCase,
    private val searchDiaryRoomUseCase: SearchDiaryRoomUseCase,
    private val isInternetAvailableUseCase: IsInternetAvailableUseCase
) : ViewModel() {

    private val _defaultFont = MutableStateFlow<GenericFontFamily?>(null)
    val defaultFont: StateFlow<GenericFontFamily?> = _defaultFont

    private val _diariesUiState = MutableStateFlow<UiState<List<DiaryEntity>>>(UiState.Idle)
    val diariesUiState: StateFlow<UiState<List<DiaryEntity>>> = _diariesUiState

    private val _uiMessageState = MutableStateFlow<UiMessage?>(null)
    val uiMessageState: StateFlow<UiMessage?> = _uiMessageState

    private val _uiSearchButtonState = MutableStateFlow(false)
    val uiSearchButtonState: StateFlow<Boolean> = _uiSearchButtonState

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
        if (isInternetAvailableUseCase()) {
            viewModelScope.launch {
                when (val result = getDiariesServerUseCase()) {
                    is Resource.Success -> {
                        result.data?.let { diariesFromServer ->
                            addNonDiaries(diariesFromServer)
                        }
                    }

                    is Resource.Error -> {
                        _uiMessageState.value = UiMessage.Error(
                            message = result.message ?: R.string.something_went_wrong,
                            statusCode = result.status
                        )
                    }
                }
            }
        } else {
            _uiMessageState.value =
                UiMessage.Error(R.string.no_internet)
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
        if (isInternetAvailableUseCase()) {
            viewModelScope.launch {
                deleteDiariesRoomUseCase(selectedDiaryEntity)
                if (deleteDiaryServerUseCase(selectedDiaryEntity.diaryId) is Resource.Success) {
                    _uiMessageState.value = UiMessage.Success(R.string.deleted_successfully)
                } else {
                    _uiMessageState.value = UiMessage.Error(R.string.something_went_wrong)
                }
            }
        } else {
            _uiMessageState.value =
                UiMessage.Error(R.string.no_internet)
        }
    }

    private fun getDefaultFont() {
        viewModelScope.launch {
            _defaultFont.value =
                FontFamilySealed.fromLabel(getFontFamilyUseCase().first()).fontFamily
        }
    }

    fun toggleSearchButtonState() {
        _uiSearchButtonState.value = !_uiSearchButtonState.value
        if (!_uiSearchButtonState.value) {
            getDiariesFromRoom()
        }
    }

    fun onSearchQueryChanged(query: String) {
        viewModelScope.launch {
            _diariesUiState.value = UiState.Loading
            searchDiaryRoomUseCase(query).collect { resultDiaryEntity ->
                _diariesUiState.value = UiState.Success(resultDiaryEntity)
            }
        }
    }

    fun clearUiMessageState() {
        _uiMessageState.value = null
    }
}