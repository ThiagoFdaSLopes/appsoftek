package com.grupo.appsoftek.data.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

data class UserAssessmentAnswer(
    val questionsId: String,
    val category: String,
    val value: String
)

data class UserAssessment(
    val id: String,
    val userId: String,
    val createdAt: String,
    val answers: List<UserAssessmentAnswer>
)

interface UserAssessmentsApiService {
    @GET("assessments/user/{userId}")
    suspend fun getUserAssessments(
        @Header("Authorization") authToken: String,
        @Path("userId") userId: String
    ): Response<List<UserAssessment>>
}
