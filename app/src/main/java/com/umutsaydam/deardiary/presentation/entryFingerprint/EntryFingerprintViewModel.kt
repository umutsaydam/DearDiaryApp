package com.umutsaydam.deardiary.presentation.entryFingerprint

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class EntryFingerprintViewModel : ViewModel() {

    private val _uiMessageState = MutableStateFlow("")
    val uiMessageState: StateFlow<String> = _uiMessageState

    fun updateUiMessageState(message: String) {
        _uiMessageState.value = message
    }

    fun clearUiMessageState() {
        _uiMessageState.value = ""
    }
}