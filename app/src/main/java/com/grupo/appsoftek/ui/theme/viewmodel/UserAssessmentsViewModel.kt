package com.grupo.appsoftek.ui.theme.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.grupo.appsoftek.data.network.UserAssessment
import com.grupo.appsoftek.data.repository.UserAssessmentsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class UserAssessmentsUiState {
    object Loading : UserAssessmentsUiState()
    data class Loaded(val assessments: List<UserAssessment>) : UserAssessmentsUiState()
    data class Error(val message: String) : UserAssessmentsUiState()
}

class UserAssessmentsViewModel(application: Application) : AndroidViewModel(application) {

    private val userAssessmentsRepository = UserAssessmentsRepository(application)

    private val _uiState = MutableStateFlow<UserAssessmentsUiState>(UserAssessmentsUiState.Loading)
    val uiState: StateFlow<UserAssessmentsUiState> = _uiState.asStateFlow()

    fun loadUserAssessments() {
        viewModelScope.launch {
            _uiState.value = UserAssessmentsUiState.Loading
            userAssessmentsRepository.getUserAssessments()
                .onSuccess { assessments ->
                    _uiState.value = UserAssessmentsUiState.Loaded(assessments)
                }
                .onFailure { throwable ->
                    _uiState.value = UserAssessmentsUiState.Error(throwable.message ?: "Erro ao carregar respostas")
                }
        }
    }

    // Função para verificar se o usuário já respondeu uma categoria específica
    fun hasAnsweredCategory(category: String): Boolean {
        println("DEBUG: hasAnsweredCategory chamada para categoria: '$category'")
        return when (val state = _uiState.value) {
            is UserAssessmentsUiState.Loaded -> {
                println("DEBUG: Estado carregado com ${state.assessments.size} assessments")
                val result = userAssessmentsRepository.hasAnsweredCategory(state.assessments, category)
                println("DEBUG: Resultado para '$category': $result")
                result
            }
            else -> {
                println("DEBUG: Estado não carregado, retornando false")
                false
            }
        }
    }

    // Função para obter as categorias já respondidas
    fun getAnsweredCategories(): Set<String> {
        println("DEBUG: getAnsweredCategories chamada")
        return when (val state = _uiState.value) {
            is UserAssessmentsUiState.Loaded -> {
                println("DEBUG: Estado carregado, obtendo categorias respondidas")
                val categories = userAssessmentsRepository.getAnsweredCategories(state.assessments)
                println("DEBUG: Categorias retornadas: $categories")
                categories
            }
            else -> {
                println("DEBUG: Estado não carregado, retornando emptySet")
                emptySet()
            }
        }
    }

    // Função para normalizar nomes de categorias (igual à do repository)
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

    // Função para obter o progresso geral (quantas categorias foram respondidas)
    fun getProgress(): Pair<Int, Int> {
        println("DEBUG: getProgress chamada")
        val answeredCategories = getAnsweredCategories()
        val totalCategories = setOf("Carga de trabalho", "Produtividade", "Comunicação", "Clima", "Liderança", "bem-estar")
        
        println("DEBUG: Progresso - Categorias respondidas: $answeredCategories")
        println("DEBUG: Progresso - Total de categorias: $totalCategories")
        
        // Normalizar as categorias para comparação
        val normalizedAnsweredCategories = answeredCategories.map { normalizeCategory(it) }.toSet()
        val normalizedTotalCategories = totalCategories.map { normalizeCategory(it) }.toSet()
        
        println("DEBUG: Progresso - Categorias respondidas normalizadas: $normalizedAnsweredCategories")
        println("DEBUG: Progresso - Total de categorias normalizadas: $normalizedTotalCategories")
        
        // Contar quantas categorias normalizadas correspondem
        val matchedCount = normalizedTotalCategories.count { totalCategory ->
            val isMatched = normalizedAnsweredCategories.contains(totalCategory)
            println("DEBUG: Categoria total '$totalCategory' foi respondida? $isMatched")
            isMatched
        }
        
        println("DEBUG: Progresso final - Respondidas: $matchedCount, Total: ${totalCategories.size}")
        
        return Pair(matchedCount, totalCategories.size)
    }
}
