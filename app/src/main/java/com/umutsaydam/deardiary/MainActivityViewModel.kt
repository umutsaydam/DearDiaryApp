package com.umutsaydam.deardiary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umutsaydam.deardiary.domain.Resource
import com.umutsaydam.deardiary.domain.useCases.local.securityUseCase.fingerPrint.IsFingerPrintEnabledUseCase
import com.umutsaydam.deardiary.domain.useCases.local.securityUseCase.pin.GetPinUseCase
import com.umutsaydam.deardiary.domain.useCases.remote.auth.TokenReusable
import com.umutsaydam.deardiary.domain.useCases.local.tokenUseCase.GetTokenUseCase
import com.umutsaydam.deardiary.presentation.navigation.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val getTokenUseCase: GetTokenUseCase,
    private val tokenReusable: TokenReusable,
    private val getPinUseCase: GetPinUseCase,
    private val isFingerPrintEnabledUseCase: IsFingerPrintEnabledUseCase
) : ViewModel() {
    private val _nextRoute = MutableStateFlow<String?>(null)
    val nextRoute: StateFlow<String?> = _nextRoute

    private val _isFingerprintEnabled = MutableStateFlow(false)
    private val _isPinEnabled = MutableStateFlow(false)

    init {
        getIsFingerprintEnabled()
        getIsPinEnabled()
        userAlreadySignedInAndTokenNotExpired()
    }

    private fun getIsPinEnabled() {
        viewModelScope.launch {
            _isPinEnabled.value = getPinUseCase().first().isNotEmpty()
        }
    }

    private fun getIsFingerprintEnabled() {
        viewModelScope.launch {
            _isFingerprintEnabled.value = isFingerPrintEnabledUseCase().first()
        }
    }

    private fun userAlreadySignedInAndTokenNotExpired() {
        viewModelScope.launch {
            if (getTokenUseCase().first() == null) {
                _nextRoute.value = Route.Auth.route
            } else {
                when (tokenReusable()) {
                    is Resource.Success -> {
                        if (_isFingerprintEnabled.value) {
                            _nextRoute.value = Route.EntryFingerprint.route
                        } else if (_isPinEnabled.value) {
                            _nextRoute.value = Route.EntryPin.createRoute(false)
                        } else {
                            _nextRoute.value = Route.Diaries.route
                        }
                    }

                    is Resource.Error -> {
                        _nextRoute.value = Route.Auth.route
                    }

                    is Resource.Loading -> {}
                }
            }
        }
    }
}