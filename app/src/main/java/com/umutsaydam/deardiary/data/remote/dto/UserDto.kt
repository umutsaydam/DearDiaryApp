package com.umutsaydam.deardiary.data.remote.dto

import com.google.gson.annotations.SerializedName
import java.util.UUID

data class UserDto(
    @SerializedName("user_id")
    val userId: UUID? = null,
    @SerializedName("username")
    val username: String,
    @SerializedName("password")
    val password: String
)