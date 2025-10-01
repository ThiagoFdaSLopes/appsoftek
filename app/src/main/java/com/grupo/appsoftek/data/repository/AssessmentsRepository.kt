package com.grupo.appsoftek.data.repository

import android.app.Application
import android.content.Context
import com.grupo.appsoftek.data.network.AssessmentsApiService
import com.grupo.appsoftek.data.network.AuthRetrofitClient
import com.grupo.appsoftek.data.network.CanAnswerResponse

class AssessmentsRepository(private val application: Application) {

    private val apiService = AuthRetrofitClient.createService(AssessmentsApiService::class.java)
    private val sharedPreferences = application.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    suspend fun canAnswer(): Result<CanAnswerResponse> {
        return try {
            val token = sharedPreferences.getString("auth_token", null)
                ?: return Result.failure(Exception("Auth token not found"))

            var userId = sharedPreferences.getString("user_id", null)
            if (userId == null) {
                userId = sharedPreferences.getString("current_user_uuid", null)
            }
            
            if (userId == null) {
                return Result.failure(Exception("User ID not found"))
            }

            val response = apiService.canAnswer("Bearer $token", userId)
            
            if (response.isSuccessful) {
                val data = response.body()
                if (data != null) {
                    Result.success(data)
                } else {
                    Result.failure(Exception("Resposta vazia da API"))
                }
            } else {
                Result.failure(Exception("Erro na API: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

