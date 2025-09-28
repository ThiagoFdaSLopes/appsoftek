package com.grupo.appsoftek.data.repository

import android.app.Application
import android.content.Context
import com.grupo.appsoftek.data.network.AuthRetrofitClient
import com.grupo.appsoftek.data.network.DailyCheckinApiService
import com.grupo.appsoftek.data.network.DailyCheckinRequest

class DailyCheckinRepository(private val application: Application) {

    private val apiService = AuthRetrofitClient.createService(DailyCheckinApiService::class.java)
    private val sharedPreferences = application.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    suspend fun submitDailyCheckin(userId: String, mood: String): Result<Unit> {
        return try {
            val token = sharedPreferences.getString("auth_token", null)
                ?: return Result.failure(Exception("Auth token not found"))

            val request = DailyCheckinRequest(userId = userId, mood = mood)
            val response = apiService.submitDailyCheckin("Bearer $token", request)

            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Erro na API: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
