package com.grupo.appsoftek.data.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

data class RegisterRequest(
    val uuid: String,
    val password: String
)

data class RegisterResponse(
    val uuid: String?,
    val message: String?
)

interface AuthApiService {
    @POST("/auth/register")
    suspend fun register(@Body body: RegisterRequest): Response<RegisterResponse>
}


