package com.grupo.appsoftek.ui.theme.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.grupo.appsoftek.data.network.QuestionDto
import com.grupo.appsoftek.data.network.AssessmentAnswerDto
import com.grupo.appsoftek.data.network.AssessmentRequest
import com.grupo.appsoftek.data.network.AssessmentsApiService
import com.grupo.appsoftek.data.network.AuthRetrofitClient
import com.grupo.appsoftek.data.repository.QuestionsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class QuestionsUiState {
    object Idle : QuestionsUiState()
    object Loading : QuestionsUiState()
    data class Loaded(val questions: List<QuestionDto>) : QuestionsUiState()
    data class Error(val message: String) : QuestionsUiState()
}

class QuestionsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = QuestionsRepository(application.applicationContext)
    private val assessmentsApi: AssessmentsApiService = AuthRetrofitClient.createService(AssessmentsApiService::class.java)

    private val _uiState = MutableStateFlow<QuestionsUiState>(QuestionsUiState.Idle)
    val uiState: StateFlow<QuestionsUiState> = _uiState.asStateFlow()

    fun loadQuestions() {
        _uiState.value = QuestionsUiState.Loading
        viewModelScope.launch {
            val result = repository.fetchQuestions()
            _uiState.value = result.fold(
                onSuccess = { QuestionsUiState.Loaded(it) },
                onFailure = { QuestionsUiState.Error(it.message ?: "Erro ao carregar perguntas") }
            )
        }
    }

    fun submitAssessment(userId: String, answers: List<AssessmentAnswerDto>, onResult: (Result<Unit>) -> Unit) {
        viewModelScope.launch {
            try {
                val prefs = getApplication<Application>().getSharedPreferences("auth_prefs", android.content.Context.MODE_PRIVATE)
                val token = prefs.getString("auth_token", null) ?: return@launch onResult(Result.failure(IllegalStateException("Token ausente")))
                val body = AssessmentRequest(userId = userId, answers = answers)
                val response = assessmentsApi.submitAssessment(
                    authorization = "Bearer $token",
                    body = body
                )
                if (response.isSuccessful) onResult(Result.success(Unit))
                else onResult(Result.failure(Exception("Erro ${response.code()}: ${response.message()}")))
            } catch (e: Exception) {
                onResult(Result.failure(e))
            }
        }
    }
}
