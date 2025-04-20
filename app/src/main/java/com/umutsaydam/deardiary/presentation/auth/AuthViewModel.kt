package com.umutsaydam.deardiary.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umutsaydam.deardiary.R
import com.umutsaydam.deardiary.domain.enums.AuthStateEnum
import com.umutsaydam.deardiary.domain.sealedStates.Resource
import com.umutsaydam.deardiary.domain.sealedStates.UiMessage
import com.umutsaydam.deardiary.domain.sealedStates.UiState
import com.umutsaydam.deardiary.domain.entity.TokenEntity
import com.umutsaydam.deardiary.domain.entity.UserEntity
import com.umutsaydam.deardiary.domain.useCases.IsInternetAvailableUseCase
import com.umutsaydam.deardiary.domain.useCases.remote.auth.UserCreateUseCase
import com.umutsaydam.deardiary.domain.useCases.remote.auth.UserLoginUseCase
import com.umutsaydam.deardiary.domain.useCases.local.tokenUseCase.SaveTokenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val createUseCase: UserCreateUseCase,
    private val loginUseCase: UserLoginUseCase,
    private val saveTokenUseCase: SaveTokenUseCase,
    private val isInternetAvailableUseCase: IsInternetAvailableUseCase
) : ViewModel() {

    private val _authState = MutableStateFlow(AuthStateEnum.LOGIN)
    val authState: StateFlow<AuthStateEnum> = _authState

    private val _authUiState = MutableStateFlow<UiState<TokenEntity>>(UiState.Idle)
    val authUiState: StateFlow<UiState<TokenEntity>> = _authUiState

    private val _uiMessageState = MutableStateFlow<UiMessage?>(null)
    val uiMessageState: StateFlow<UiMessage?> = _uiMessageState

    fun createUser(username: String, password: String, passwordConfirm: String) {
        if (username.isBlank() && password.isBlank() && passwordConfirm.isBlank()) {
            _uiMessageState.value = UiMessage.Error(R.string.username_passwords_cannot_empty)
            return
        }

        if (password != passwordConfirm) {
            _uiMessageState.value = UiMessage.Error(R.string.passwords_does_not_match)
            return
        }

        if (isInternetAvailableUseCase()) {
            viewModelScope.launch {
                _authUiState.value = UiState.Loading
                val result = createUseCase(UserEntity(username = username, password = password))
                when (result) {
                    is Resource.Success -> {
                        switchLoginState()
                        _uiMessageState.value = UiMessage.Success(R.string.signed_up_successfully)
                        _authUiState.value = UiState.Idle
                    }

                    is Resource.Error -> {
                        _uiMessageState.value =
                            UiMessage.Error(result.message ?: R.string.something_went_wrong)
                        _authUiState.value = UiState.Idle
                    }

                    is Resource.Loading -> {
                        UiState.Loading
                    }
                }
            }
        } else {
            _uiMessageState.value =
                UiMessage.Error(R.string.no_internet)
        }
    }

    fun loginUser(username: String, password: String) {
        if (username.isBlank() || password.isBlank()) {
            _uiMessageState.value = UiMessage.Error(R.string.username_passwords_cannot_empty)
            return
        }

        viewModelScope.launch {
            if (isInternetAvailableUseCase()) {
                _authUiState.value = UiState.Loading
                when (val result =
                    loginUseCase(UserEntity(username = username, password = password))) {
                    is Resource.Success -> {
                        result.data?.let { saveTokenUseCase(it.token) }
                        _authUiState.value = UiState.Success()
                    }

                    is Resource.Error -> {
                        _uiMessageState.value =
                            UiMessage.Error(result.message ?: R.string.something_went_wrong)
                        _authUiState.value = UiState.Idle
                    }

                    is Resource.Loading -> {
                        UiState.Loading
                    }
                }
            } else {
                _uiMessageState.value =
                    UiMessage.Error(R.string.no_internet)
            }
        }
    }

    fun switchLoginState() {
        _authState.value = AuthStateEnum.LOGIN
    }

    fun switchRegisterState() {
        _authState.value = AuthStateEnum.REGISTER
    }

    fun clearUiMessageState() {
        _uiMessageState.value = null
    }
}