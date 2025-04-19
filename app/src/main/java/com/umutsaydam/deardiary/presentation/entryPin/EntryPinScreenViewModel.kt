package com.umutsaydam.deardiary.presentation.entryPin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umutsaydam.deardiary.domain.enums.PinStateEnum
import com.umutsaydam.deardiary.domain.useCases.local.securityUseCase.pin.GetPinUseCase
import com.umutsaydam.deardiary.util.Constants.PIN_LENGTH
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EntryPinScreenViewModel @Inject constructor(
    private val getPinUseCase: GetPinUseCase
) : ViewModel() {

    private val _pinState = MutableStateFlow<PinStateEnum?>(null)
    val pinState: StateFlow<PinStateEnum?> = _pinState

    private val _savedPin = MutableStateFlow("")

    private val _enteredPin = MutableStateFlow("")
    val enteredPin: StateFlow<String> = _enteredPin

    init {
        getSavedPin()
    }

    private fun getSavedPin() {
        viewModelScope.launch {
            _savedPin.value = getPinUseCase().first()
            updatePinState(PinStateEnum.ENTER_CURRENT_PIN)
        }
    }

    private fun updatePinState(currentPinState: PinStateEnum) {
        _pinState.value = currentPinState
    }

    private fun checkPinsMatch(): Boolean {
        return _savedPin.value == _enteredPin.value
    }

    fun updateEnteredPin(pin: String) {
        _enteredPin.value = pin

        if (_enteredPin.value.length == PIN_LENGTH) {
            if (checkPinsMatch()) {
                updatePinState(PinStateEnum.DONE)
            } else {
                _enteredPin.value = ""
            }
        }
    }
}