package com.grupo.appsoftek.data.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

data class CategoryStatus(
    val category: String,
    val percentage: Int,
    val status: String
)

data class DashboardResponse(
    val categoryStatuses: List<CategoryStatus>
)

interface DashboardApiService {
    @GET("dashboard")
    suspend fun getDashboardData(
        @Header("Authorization") authToken: String,
        @Query("userId") userId: String
    ): Response<DashboardResponse>
}
