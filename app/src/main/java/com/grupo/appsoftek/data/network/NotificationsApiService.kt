package com.grupo.appsoftek.data.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

data class NotificationItem(
    val id: String,
    val type: String,
    val title: String,
    val message: String
)

interface NotificationsApiService {
    @GET("support-notifications")
    suspend fun getSupportNotifications(
        @Header("Authorization") authToken: String
    ): Response<List<NotificationItem>>
}


