package com.grupo.appsoftek.data.repository

import com.grupo.appsoftek.data.model.Quote
import com.grupo.appsoftek.data.network.QuoteApiService
import com.grupo.appsoftek.data.network.RetrofitClient
import kotlin.random.Random

class QuoteRepository {
    private val apiService = RetrofitClient.createService(QuoteApiService::class.java)


    suspend fun getRandomQuote(): Result<Quote> {
        return try {
            val response = apiService.getRandomQuote()

            if (response.isSuccessful) {
                val quoteResponse = response.body()

                if (quoteResponse != null && quoteResponse.results.isNotEmpty()) {
                    // Escolha uma citação aleatória da lista de resultados
                    val randomIndex = Random.nextInt(quoteResponse.results.size)
                    val randomQuote = quoteResponse.results[randomIndex]
                    Result.success(randomQuote)
                } else {
                    Result.failure(Exception("Nenhuma citação encontrada"))
                }
            } else {
                Result.failure(Exception("Erro na API: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}