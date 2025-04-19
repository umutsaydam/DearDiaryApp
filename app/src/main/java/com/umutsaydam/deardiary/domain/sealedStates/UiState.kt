package com.umutsaydam.deardiary.domain.sealedStates

sealed class UiState<out T> {
    data object Idle : UiState<Nothing>()
    data object Loading : UiState<Nothing>()
    data class Success<out T>(val data: T? = null) : UiState<T>()
}
