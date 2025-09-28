package com.grupo.appsoftek.data.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

data class AssessmentAnswerDto(
    val questionId: String,
    val category: String,
    val value: String
)

data class AssessmentRequest(
    val userId: String,
    val answers: List<AssessmentAnswerDto>
)

interface AssessmentsApiService {
    @POST("assessments")
    suspend fun submitAssessment(
        @Header("Authorization") authorization: String,
        @Body body: AssessmentRequest
    ): Response<Unit>
}




