package com.grupo.appsoftek.data.repository

import android.app.Application
import android.content.Context
import com.grupo.appsoftek.data.network.AuthRetrofitClient
import com.grupo.appsoftek.data.network.DashboardApiService
import com.grupo.appsoftek.data.network.DashboardResponse

class DashboardRepository(private val application: Application) {

    private val apiService = AuthRetrofitClient.createService(DashboardApiService::class.java)
    private val sharedPreferences = application.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    suspend fun getDashboardData(): Result<DashboardResponse> {
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

            val response = apiService.getDashboardData("Bearer $token", userId)

            if (response.isSuccessful) {
                val dashboardData = response.body()
                println("DEBUG: Dashboard API response: $dashboardData")
                if (dashboardData != null) {
                    println("DEBUG: categoryStatuses: ${dashboardData.categoryStatuses}")
                    Result.success(dashboardData)
                } else {
                    println("DEBUG: Dashboard data is null")
                    Result.failure(Exception("Resposta vazia da API"))
                }
            } else {
                println("DEBUG: Dashboard API error: ${response.code()} - ${response.message()}")
                Result.failure(Exception("Erro na API: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
