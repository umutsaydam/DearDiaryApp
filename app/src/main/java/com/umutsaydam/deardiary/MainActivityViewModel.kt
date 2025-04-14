package com.umutsaydam.deardiary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umutsaydam.deardiary.domain.Resource
import com.umutsaydam.deardiary.domain.useCase.auth.TokenReusable
import com.umutsaydam.deardiary.domain.useCase.tokenUseCase.GetTokenUseCase
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
    private val tokenReusable: TokenReusable
) : ViewModel() {
    private val _nextRoute = MutableStateFlow<String?>(null)
    val nextRoute: StateFlow<String?> = _nextRoute

    init {
        userAlreadySignedInAndTokenNotExpired()
    }

    private fun userAlreadySignedInAndTokenNotExpired() {
        viewModelScope.launch {
            if (getTokenUseCase().first() == null) {
                _nextRoute.value = Route.Auth.route
            } else {
                when (tokenReusable()) {
                    is Resource.Success -> {
                        _nextRoute.value = Route.Diaries.route
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