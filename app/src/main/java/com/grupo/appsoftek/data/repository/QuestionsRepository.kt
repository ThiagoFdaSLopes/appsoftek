package com.grupo.appsoftek.data.repository

import android.content.Context
import com.grupo.appsoftek.data.network.AuthRetrofitClient
import com.grupo.appsoftek.data.network.QuestionDto
import com.grupo.appsoftek.data.network.QuestionsApiService

class QuestionsRepository(private val context: Context) {
    private val api: QuestionsApiService = AuthRetrofitClient.createService(QuestionsApiService::class.java)

    private fun getSavedToken(): String? {
        val prefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        return prefs.getString("auth_token", null)
    }

    suspend fun fetchQuestions(): Result<List<QuestionDto>> {
        return try {
            val token = getSavedToken() ?: return Result.failure(IllegalStateException("Token ausente"))
            val response = api.getQuestions(authorization = "Bearer $token")
            if (response.isSuccessful) {
                val list = response.body().orEmpty()
                val deduped = list.distinctBy { it.id }
                Result.success(deduped)
            } else {
                Result.failure(Exception("Erro ${response.code()}: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}


