package com.grupo.appsoftek.data.repository

import android.app.Application
import android.content.Context
import com.grupo.appsoftek.data.network.AuthRetrofitClient
import com.grupo.appsoftek.data.network.UserAssessmentsApiService
import com.grupo.appsoftek.data.network.UserAssessment

class UserAssessmentsRepository(private val application: Application) {

    private val apiService = AuthRetrofitClient.createService(UserAssessmentsApiService::class.java)
    private val sharedPreferences = application.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    suspend fun getUserAssessments(): Result<List<UserAssessment>> {
        return try {
            val token = sharedPreferences.getString("auth_token", null)
                ?: return Result.failure(Exception("Auth token not found"))

            var userId = sharedPreferences.getString("user_id", null)
            if (userId == null) {
                userId = sharedPreferences.getString("current_user_uuid", null)
            }
            
            if (userId == null) {
                return Result.failure(Exception("User ID not found"))
            }

            val response = apiService.getUserAssessments("Bearer $token", userId)

            if (response.isSuccessful) {
                val assessments = response.body() ?: emptyList()
                Result.success(assessments)
            } else {
                Result.failure(Exception("Erro na API: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Função para normalizar nomes de categorias (remover espaços extras, acentos, etc.)
    private fun normalizeCategory(category: String): String {
        return category.trim()
            .lowercase()
            .replace("ç", "c")
            .replace("ã", "a")
            .replace("õ", "o")
            .replace("á", "a")
            .replace("é", "e")
            .replace("í", "i")
            .replace("ó", "o")
            .replace("ú", "u")
    }

    // Função para verificar se o usuário já respondeu uma categoria específica
    fun hasAnsweredCategory(assessments: List<UserAssessment>, category: String): Boolean {
        println("DEBUG: Verificando categoria: '$category'")
        println("DEBUG: Total de assessments: ${assessments.size}")
        
        if (assessments.isEmpty()) {
            println("DEBUG: Nenhum assessment encontrado")
            return false
        }
        
        val normalizedCategory = normalizeCategory(category)
        println("DEBUG: Categoria normalizada: '$normalizedCategory'")
        
        assessments.forEach { assessment ->
            println("DEBUG: Assessment ID: ${assessment.id}")
            assessment.answers.forEach { answer ->
                println("DEBUG: Answer - category: '${answer.category}', value: '${answer.value}'")
                println("DEBUG: Answer category normalizada: '${normalizeCategory(answer.category)}'")
            }
        }
        
        val hasAnswered = assessments.any { assessment ->
            assessment.answers.any { answer ->
                val normalizedAnswerCategory = normalizeCategory(answer.category)
                val matches = normalizedAnswerCategory == normalizedCategory
                println("DEBUG: Comparando normalizado '$normalizedAnswerCategory' com '$normalizedCategory': $matches")
                matches
            }
        }
        
        println("DEBUG: hasAnswered para '$category': $hasAnswered")
        return hasAnswered
    }

    // Função para obter as categorias já respondidas
    fun getAnsweredCategories(assessments: List<UserAssessment>): Set<String> {
        println("DEBUG: getAnsweredCategories - Total assessments: ${assessments.size}")
        
        if (assessments.isEmpty()) {
            println("DEBUG: Nenhum assessment encontrado, retornando emptySet")
            return emptySet()
        }
        
        val categories = assessments.flatMap { assessment ->
            println("DEBUG: Processando assessment ${assessment.id} com ${assessment.answers.size} respostas")
            assessment.answers.map { answer ->
                println("DEBUG: Categoria encontrada: '${answer.category}'")
                // Manter a categoria original, não normalizada, para o retorno
                answer.category
            }
        }.toSet()

        println("DEBUG: Categorias respondidas final: $categories")
        
        // Vamos também mostrar as categorias normalizadas para debug
        val normalizedCategories = categories.map { normalizeCategory(it) }.toSet()
        println("DEBUG: Categorias normalizadas: $normalizedCategories")
        
        return categories
    }
}
