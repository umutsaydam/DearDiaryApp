package com.umutsaydam.deardiary.domain

sealed class UiMessage {
    data class Success(val message: String) : UiMessage()
    data class Error(val message: String, val statusCode: Int? = null) : UiMessage()
}