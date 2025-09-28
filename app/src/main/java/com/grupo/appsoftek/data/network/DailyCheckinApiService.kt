package com.grupo.appsoftek.data.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

data class DailyCheckinRequest(
    val userId: String,
    val mood: String
)

interface DailyCheckinApiService {
    @POST("daily-checkins")
    suspend fun submitDailyCheckin(
        @Header("Authorization") authToken: String,
        @Body request: DailyCheckinRequest
    ): Response<Unit>
}
