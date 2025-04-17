package com.umutsaydam.deardiary.presentation.settings.fingerPrintSettings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umutsaydam.deardiary.domain.useCases.local.securityUseCase.fingerPrint.IsFingerPrintEnabledUseCase
import com.umutsaydam.deardiary.domain.useCases.local.securityUseCase.fingerPrint.SetFingerPrintEnabledUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FingerPrintViewModel @Inject constructor(
    private val isFingerPrintEnabledUseCase: IsFingerPrintEnabledUseCase,
    private val setFingerPrintEnabledUseCase: SetFingerPrintEnabledUseCase
) : ViewModel() {

    private val _isFingerPrintEnabled = MutableStateFlow(false)

    private val _uiMessageState = MutableStateFlow("")
    val uiMessageState: StateFlow<String> = _uiMessageState

    init {
        getIsFingerPrintEnabled()
    }

    private fun getIsFingerPrintEnabled() {
        viewModelScope.launch {
            isFingerPrintEnabledUseCase().collect { isEnabled ->
                _isFingerPrintEnabled.value = isEnabled
            }
        }
    }

    fun setIsFingerPrintEnabled() {
        viewModelScope.launch {
            val isEnabled = !_isFingerPrintEnabled.value
            setFingerPrintEnabledUseCase(isEnabled)
            if (isEnabled) {
                _uiMessageState.value = "Your fingerprint has been added."
            } else {
                _uiMessageState.value = "Your fingerprint has been removed."
            }
        }
    }

    fun updateUiMessageState(message: String) {
        _uiMessageState.value = message
    }

    fun clearUiMessageState() {
        _uiMessageState.value = ""
    }
}