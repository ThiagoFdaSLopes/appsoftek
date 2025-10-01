package com.grupo.appsoftek.data.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

data class AssessmentAnswerDto(
    val questionId: String,
    val category: String,
    val value: String
)

data class AssessmentRequest(
    val userId: String,
    val answers: List<AssessmentAnswerDto>
)

data class CanAnswerResponse(
    val canAnswer: Boolean,
    val lastAnswerDate: String?,
    val message: String
)

interface AssessmentsApiService {
    @GET("assessments/user/{userId}/can-answer")
    suspend fun canAnswer(
        @Header("Authorization") authorization: String,
        @Path("userId") userId: String
    ): Response<CanAnswerResponse>
    
    @POST("assessments")
    suspend fun submitAssessment(
        @Header("Authorization") authorization: String,
        @Body body: AssessmentRequest
    ): Response<Unit>
}




