package com.grupo.appsoftek.data.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

data class QuestionDto(
    val id: String,
    val text: String,
    val category: String
)

interface QuestionsApiService {
    @GET("questions")
    suspend fun getQuestions(
        @Header("Authorization") authorization: String
    ): Response<List<QuestionDto>>
}




