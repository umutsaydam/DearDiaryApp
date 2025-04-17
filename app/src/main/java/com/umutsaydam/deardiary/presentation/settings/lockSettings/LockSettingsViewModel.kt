package com.umutsaydam.deardiary.presentation.settings.lockSettings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umutsaydam.deardiary.domain.useCases.local.securityUseCase.fingerPrint.IsFingerPrintEnabledUseCase
import com.umutsaydam.deardiary.domain.useCases.local.securityUseCase.pin.GetPinUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LockSettingsViewModel @Inject constructor(
    private val getPinUseCase: GetPinUseCase,
    private val isFingerPrintEnabledUseCase: IsFingerPrintEnabledUseCase
) : ViewModel() {

    private val _isPinEnable = MutableStateFlow(false)
    val isPinEnable: StateFlow<Boolean> = _isPinEnable

    private val _isFingerPrintEnabled = MutableStateFlow(false)
    val isFingerPrintEnabled = _isFingerPrintEnabled

    private val _uiMessageState = MutableStateFlow("")
    val uiMessageState: StateFlow<String> = _uiMessageState

    init {
        getPin()
        getIsFingerPrintEnabled()
    }

    private fun getPin() {
        viewModelScope.launch {
            getPinUseCase().collect { value ->
                _isPinEnable.value = value.isNotEmpty()
            }
        }
    }

    private fun getIsFingerPrintEnabled() {
        viewModelScope.launch {
            isFingerPrintEnabledUseCase().collect { isEnable ->
                _isFingerPrintEnabled.value = isEnable
            }
        }
    }

    fun clearUiMessageState() {
        _uiMessageState.value = ""
    }

    fun navigateSetPinScreenIfFingerprintNotEnable(): Boolean {
        if(_isPinEnable.value){
            if (_isFingerPrintEnabled.value){
                _uiMessageState.value = "First you must remove fingerprint lock."
                return false
            }
        }
        return true
    }

    fun navigateSetFingerprintIfPinEnable(): Boolean {
        if (!_isPinEnable.value){
            _uiMessageState.value = "First you must add a pin."
            return false
        }
        return true
    }
}