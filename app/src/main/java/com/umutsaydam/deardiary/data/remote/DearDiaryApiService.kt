package com.umutsaydam.deardiary.data.remote

import com.umutsaydam.deardiary.data.remote.dto.TokenDto
import com.umutsaydam.deardiary.data.remote.dto.UserDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface DearDiaryApiService {

    @POST("user/create")
    suspend fun userCreate(@Body userDto: UserDto): Response<Unit>

    @POST("user/login")
    suspend fun userLogin(@Body userDto: UserDto): Response<TokenDto>

    @POST("user/token-reusable")
    suspend fun tokenReusable(): Response<Unit>
}