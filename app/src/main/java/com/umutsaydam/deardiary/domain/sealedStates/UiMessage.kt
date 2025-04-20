package com.umutsaydam.deardiary.domain.sealedStates

sealed class UiMessage {
    data class Success(val message: Int) : UiMessage()
    data class Error(val message: Int, val statusCode: Int? = null) : UiMessage()
}