package com.grupo.appsoftek.data.repository

import com.grupo.appsoftek.data.model.Quote
import com.grupo.appsoftek.data.network.QuoteApiService
import com.grupo.appsoftek.data.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class QuoteRepository {
    private val apiService = RetrofitClient.createService(QuoteApiService::class.java)
    private val apiToken = "Token YOUR_API_TOKEN" // coloque seu token real aqui

    suspend fun getRandomQuote(): Result<Quote> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getRandomQuote(token = apiToken)
                if (response.isSuccessful) {
                    val body = response.body()
                    val results = body?.results
                    if (!results.isNullOrEmpty()) {
                        Result.success(results[0])
                    } else {
                        Result.failure(Exception("Nenhuma citação encontrada"))
                    }
                } else {
                    Result.failure(Exception("Erro ao buscar citação: ${response.code()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}
