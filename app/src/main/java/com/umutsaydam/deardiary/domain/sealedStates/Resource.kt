package com.umutsaydam.deardiary.domain.sealedStates

sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null,
    val status: Int? = null
) {
    class Loading<T> : Resource<T>()
    class Success<T>(data: T? = null) : Resource<T>(data)
    class Error<T>(status: Int? = null, message: String? = null) :
        Resource<T>(status = status, message = message)
}