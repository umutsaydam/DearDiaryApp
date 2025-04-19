package com.umutsaydam.deardiary.presentation.settings.pinSettings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umutsaydam.deardiary.domain.enums.PinStateEnum
import com.umutsaydam.deardiary.domain.useCases.local.securityUseCase.pin.GetPinUseCase
import com.umutsaydam.deardiary.domain.useCases.local.securityUseCase.pin.SetPinUseCase
import com.umutsaydam.deardiary.util.Constants.PIN_LENGTH
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PinSettingsViewModel @Inject constructor(
    private val getPinUseCase: GetPinUseCase,
    private val setPinUseCase: SetPinUseCase,
) : ViewModel() {
    private val _pin = MutableStateFlow("")

    private val _pinState = MutableStateFlow<PinStateEnum?>(null)
    val pinState: StateFlow<PinStateEnum?> = _pinState

    private val _pinText = MutableStateFlow("")
    val pinText: StateFlow<String> = _pinText

    private val _pinTextConfirm = MutableStateFlow("")
    val pinTextConfirm: StateFlow<String> = _pinTextConfirm

    private val _uiMessageState = MutableStateFlow("")
    val uiMessageState: StateFlow<String> = _uiMessageState

    init {
        getPin()
    }

    private fun getPin() {
        viewModelScope.launch {
            getPinUseCase().collectLatest { pin ->
                _pin.value = pin
                if (pin.isEmpty()) {
                    updatePinState(PinStateEnum.ENTER_FIRST)
                } else {
                    updatePinState(PinStateEnum.ENTER_CURRENT_PIN)
                }
            }
        }
    }

    private fun setPin(newPin: String) {
        viewModelScope.launch {
            setPinUseCase(newPin)
        }
    }

    private fun updatePinState(newPinState: PinStateEnum) {
        _pinState.value = newPinState
    }

    private fun removePinIfMatches() {
        if (_pin.value == _pinText.value) {
            setPin("")
            _uiMessageState.value = "Your pin has been removed."
            updatePinState(PinStateEnum.DONE)
        } else {
            _pinText.value = ""
            _uiMessageState.value = "Pins does not match."
        }
    }

    private fun savePinIfMatches() {
        if (pinText.value == pinTextConfirm.value) {
            setPin(pinText.value)
            _uiMessageState.value = "Your pin has been saved."
            updatePinState(PinStateEnum.DONE)
        } else {
            _pinText.value = ""
            _pinTextConfirm.value = ""
            _uiMessageState.value = "Pins does not match."
            updatePinState(PinStateEnum.ENTER_FIRST)
        }
    }

    fun updatePinText(value: String) {
        _pinText.value = value
        if (_pinText.value.length == PIN_LENGTH) {
            if (_pinState.value == PinStateEnum.ENTER_CURRENT_PIN) {
                removePinIfMatches()
            } else {
                updatePinState(PinStateEnum.CONFIRM_PIN)
            }
        }
    }

    fun updatePinTextConfirm(value: String) {
        _pinTextConfirm.value = value
        if (_pinTextConfirm.value.length == PIN_LENGTH) {
            savePinIfMatches()
        }
    }

    fun clearUiMessageState() {
        _uiMessageState.value = ""
    }
}