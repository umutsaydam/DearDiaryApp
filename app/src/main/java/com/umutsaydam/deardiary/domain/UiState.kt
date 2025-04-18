package com.umutsaydam.deardiary.domain

sealed class UiState<out T> {
    data object Idle : UiState<Nothing>()
    data object Loading : UiState<Nothing>()
    data class Success<out T>(val data: T? = null) : UiState<T>()
}
