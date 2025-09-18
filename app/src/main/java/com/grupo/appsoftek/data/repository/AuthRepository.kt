package com.grupo.appsoftek.data.repository

import com.grupo.appsoftek.data.network.AuthApiService
import com.grupo.appsoftek.data.network.AuthRetrofitClient
import com.grupo.appsoftek.data.network.RegisterRequest
import com.grupo.appsoftek.data.network.RegisterResponse

class AuthRepository(
    private val api: AuthApiService = AuthRetrofitClient.createService(AuthApiService::class.java)
) {
    suspend fun register(uuid: String, password: String): Result<RegisterResponse> {
        return try {
            val response = api.register(RegisterRequest(uuid = uuid, password = password))
            if (response.isSuccessful) {
                Result.success(response.body() ?: RegisterResponse(uuid = uuid, message = null))
            } else {
                Result.failure(Exception("Erro ${response.code()}: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}


