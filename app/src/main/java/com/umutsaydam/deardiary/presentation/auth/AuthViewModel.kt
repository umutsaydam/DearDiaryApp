package com.umutsaydam.deardiary.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umutsaydam.deardiary.domain.AuthStateEnum
import com.umutsaydam.deardiary.domain.Resource
import com.umutsaydam.deardiary.domain.UiMessage
import com.umutsaydam.deardiary.domain.UiState
import com.umutsaydam.deardiary.domain.entity.TokenEntity
import com.umutsaydam.deardiary.domain.entity.UserEntity
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
) : ViewModel() {

    private val _authState = MutableStateFlow(AuthStateEnum.LOGIN)
    val authState: StateFlow<AuthStateEnum> = _authState

    private val _authUiState = MutableStateFlow<UiState<TokenEntity>>(UiState.Idle)
    val authUiState: StateFlow<UiState<TokenEntity>> = _authUiState

    private val _uiMessageState = MutableStateFlow<UiMessage?>(null)
    val uiMessageState: StateFlow<UiMessage?> = _uiMessageState

    fun createUser(username: String, password: String, passwordConfirm: String) {
        if (username.isBlank() && password.isBlank() && passwordConfirm.isBlank()) {
            _uiMessageState.value = UiMessage.Error("Username, passwords can not be empty.")
            return
        }

        if (password != passwordConfirm) {
            _uiMessageState.value = UiMessage.Error("Passwords does not match.")
            return
        }

        viewModelScope.launch {
            _authUiState.value = UiState.Loading
            val result = createUseCase(UserEntity(username = username, password = password))
            when (result) {
                is Resource.Success -> {
                    switchLoginState()
                    _uiMessageState.value = UiMessage.Success("Signed up Successfully!")
                    _authUiState.value = UiState.Idle
                }

                is Resource.Error -> {
                    _uiMessageState.value =
                        UiMessage.Error(result.message ?: "Something went wrong.")
                    _authUiState.value = UiState.Idle
                }

                is Resource.Loading -> {
                    UiState.Loading
                }
            }
        }
    }

    fun loginUser(username: String, password: String) {
        if (username.isBlank() || password.isBlank()) {
            _uiMessageState.value = UiMessage.Error("Username, password can not be empty.")
            return
        }

        viewModelScope.launch {
            _authUiState.value = UiState.Loading
            when (val result = loginUseCase(UserEntity(username = username, password = password))) {
                is Resource.Success -> {
                    result.data?.let { saveTokenUseCase(it.token) }
                    _authUiState.value = UiState.Success()
                }

                is Resource.Error -> {
                    _uiMessageState.value =
                        UiMessage.Error(result.message ?: "Something went wrong.")
                    _authUiState.value = UiState.Idle
                }

                is Resource.Loading -> {
                    UiState.Loading
                }
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