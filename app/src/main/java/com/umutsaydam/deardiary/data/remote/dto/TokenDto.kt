package com.umutsaydam.deardiary.data.remote.dto

import com.google.gson.annotations.SerializedName

data class TokenDto(
    @SerializedName("token")
    val token: String
)
