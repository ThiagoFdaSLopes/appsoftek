package com.grupo.appsoftek.data.repository

import android.app.Application
import android.content.Context
import com.grupo.appsoftek.data.network.AuthRetrofitClient
import com.grupo.appsoftek.data.network.NotificationsApiService
import com.grupo.appsoftek.data.network.NotificationItem

class NotificationsRepository(private val application: Application) {

    private val apiService = AuthRetrofitClient.createService(NotificationsApiService::class.java)
    private val sharedPreferences = application.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    suspend fun getNotifications(): Result<List<NotificationItem>> {
        return try {
            val token = sharedPreferences.getString("auth_token", null)
                ?: return Result.failure(Exception("Auth token not found"))

            val response = apiService.getSupportNotifications("Bearer $token")
            if (response.isSuccessful) {
                Result.success(response.body().orEmpty())
            } else {
                Result.failure(Exception("Erro na API: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}


