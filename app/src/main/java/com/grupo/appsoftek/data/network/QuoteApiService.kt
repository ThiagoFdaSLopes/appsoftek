package com.grupo.appsoftek.data.network

import com.grupo.appsoftek.data.model.QuoteResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface QuoteApiService {
    @GET("quotes/")
    suspend fun getRandomQuote(
        @Query("format") format: String = "json",
        @Query("lang") lang: String = "pt",
        @Query("limit") limit: String = "25",
    ): Response<QuoteResponse>
}