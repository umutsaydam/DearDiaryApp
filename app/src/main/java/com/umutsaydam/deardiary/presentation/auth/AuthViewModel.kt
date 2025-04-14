package com.umutsaydam.deardiary.presentation.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umutsaydam.deardiary.domain.AuthStateEnum
import com.umutsaydam.deardiary.domain.Resource
import com.umutsaydam.deardiary.domain.entity.TokenEntity
import com.umutsaydam.deardiary.domain.entity.UserEntity
import com.umutsaydam.deardiary.domain.useCase.auth.UserCreateUseCase
import com.umutsaydam.deardiary.domain.useCase.auth.UserLoginUseCase
import com.umutsaydam.deardiary.domain.useCase.tokenUseCase.SaveTokenUseCase
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

    private val _uiMessageState = MutableStateFlow("")
    val uiMessageState: StateFlow<String> = _uiMessageState

    private val _createUserResource = MutableStateFlow<Resource<Unit>>(Resource.Loading())

    private val _loginUserResource = MutableStateFlow<Resource<TokenEntity>>(Resource.Loading())
    val loginUserResource: StateFlow<Resource<TokenEntity>> = _loginUserResource

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun createUser(username: String, password: String, passwordConfirm: String) {
        if (username.isNotEmpty() && password.isNotEmpty() && passwordConfirm.isNotEmpty()) {
            if (password == passwordConfirm) {
                viewModelScope.launch {
                    _createUserResource.value =
                        createUseCase(UserEntity(username = username, password = password))
                    when (_createUserResource.value) {
                        is Resource.Success -> {
                            _uiMessageState.value = "Signed up successfully!"
                            switchLoginState()
                        }

                        is Resource.Error -> {
                            _createUserResource.value.message?.let { _uiMessageState.value = it }
                        }

                        is Resource.Loading -> {
                            _isLoading.value = true
                        }
                    }
                }
            } else {
                _uiMessageState.value = "Passwords does not match."
            }
        } else {
            _uiMessageState.value = "Username, passwords can not be empty."
        }
    }

    fun loginUser(username: String, password: String) {
        if (username.isNotEmpty() && password.isNotEmpty()) {
            viewModelScope.launch {
                _loginUserResource.value =
                    loginUseCase(UserEntity(username = username, password = password))
                when (_loginUserResource.value) {
                    is Resource.Success -> {
                        _uiMessageState.value = "Signed in successfully!"
                        Log.i("R/T", "viewmodel: ${_loginUserResource.value.data}")
                        _loginUserResource.value.data?.let { saveTokenUseCase(token = it.token) }
                    }

                    is Resource.Error -> {
                        _loginUserResource.value.message?.let { _uiMessageState.value = it }
                    }

                    is Resource.Loading -> {
                        _isLoading.value = true
                    }
                }
            }
        } else {
            _uiMessageState.value = "Username, password can not be empty."
        }
    }

    fun switchLoginState() {
        _authState.value = AuthStateEnum.LOGIN
    }

    fun switchRegisterState() {
        _authState.value = AuthStateEnum.REGISTER
    }

    fun clearUiMessageState() {
        _uiMessageState.value = ""
    }
}