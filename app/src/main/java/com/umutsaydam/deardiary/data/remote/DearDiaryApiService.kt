package com.umutsaydam.deardiary.data.remote

import com.umutsaydam.deardiary.data.remote.dto.DiaryDto
import com.umutsaydam.deardiary.data.remote.dto.TokenDto
import com.umutsaydam.deardiary.data.remote.dto.UserDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import java.util.UUID

interface DearDiaryApiService {

    @POST("user/create")
    suspend fun userCreate(@Body userDto: UserDto): Response<Unit>

    @POST("user/login")
    suspend fun userLogin(@Body userDto: UserDto): Response<TokenDto>

    @POST("user/logout")
    suspend fun userLogout(): Response<Unit>

    @POST("user/token-reusable")
    suspend fun tokenReusable(): Response<Unit>

    @GET("diary/get-diaries")
    suspend fun getDiaries(): Response<List<DiaryDto>>

    @POST("diary/save-diary")
    suspend fun saveDiary(@Body diaryDto: DiaryDto): Response<DiaryDto>

    @DELETE("diary/delete-diary/{diary_id}")
    suspend fun deleteDiary(@Path("diary_id") diaryId: UUID): Response<Unit>

    @PATCH("diary/update-diary")
    suspend fun updateDiary(@Body diaryDto: DiaryDto): Response<DiaryDto>
}