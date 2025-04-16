package com.umutsaydam.deardiary.presentation.diaries

import androidx.compose.ui.text.font.GenericFontFamily
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umutsaydam.deardiary.domain.Resource
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

    private val _diariesList = MutableStateFlow<List<DiaryEntity>>(emptyList())
    val diariesList: StateFlow<List<DiaryEntity>> = _diariesList

    private val _uiMessageState = MutableStateFlow("")
    val uiMessageState: StateFlow<String> = _uiMessageState

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isTokenExpired = MutableStateFlow(false)
    val isTokenExpired: StateFlow<Boolean> = _isTokenExpired

    init {
        getDefaultFont()
        getDiariesFromRoom()
        getDiariesFromServer()
    }

    private fun getDiariesFromRoom() {
        _isLoading.value = true
        viewModelScope.launch {
            getDiariesRoomUseCase().collect { diaries ->
                _diariesList.value = diaries
                _isLoading.value = false
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
                    val message = result.message
                    val statusCode = result.status
                    if (message != null) {
                        _uiMessageState.value = message
                    }
                    if (statusCode != null && statusCode == 401) {
                        _isTokenExpired.value = true
                    }
                }

                is Resource.Loading -> {}
            }
        }
    }

    private fun addNonDiaries(diariesFromServer: List<DiaryEntity>) {
        val localIds = _diariesList.value.map { it.diaryId }.toSet()
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
                _uiMessageState.value = "Deleted successfully."
            } else {
                _uiMessageState.value = "Something went wrong."
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
        _uiMessageState.value = ""
    }
}