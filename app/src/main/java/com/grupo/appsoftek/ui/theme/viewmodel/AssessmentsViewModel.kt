package com.grupo.appsoftek.ui.theme.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.grupo.appsoftek.data.network.CanAnswerResponse
import com.grupo.appsoftek.data.repository.AssessmentsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class CanAnswerState {
    data object Idle : CanAnswerState()
    data object Loading : CanAnswerState()
    data class Success(val data: CanAnswerResponse) : CanAnswerState()
    data class Error(val message: String) : CanAnswerState()
}

class AssessmentsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = AssessmentsRepository(application)

    private val _canAnswerState = MutableStateFlow<CanAnswerState>(CanAnswerState.Idle)
    val canAnswerState: StateFlow<CanAnswerState> = _canAnswerState

    fun checkCanAnswer() {
        viewModelScope.launch {
            _canAnswerState.value = CanAnswerState.Loading
            val result = repository.canAnswer()
            
            _canAnswerState.value = result.fold(
                onSuccess = { CanAnswerState.Success(it) },
                onFailure = { CanAnswerState.Error(it.message ?: "Erro desconhecido") }
            )
        }
    }

    fun resetState() {
        _canAnswerState.value = CanAnswerState.Idle
    }
}

