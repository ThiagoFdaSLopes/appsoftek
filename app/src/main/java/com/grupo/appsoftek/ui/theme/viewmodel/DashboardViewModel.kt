package com.grupo.appsoftek.ui.theme.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.grupo.appsoftek.data.network.CategoryStatus
import com.grupo.appsoftek.data.network.DashboardResponse
import com.grupo.appsoftek.data.repository.DashboardRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class DashboardUiState {
    object Loading : DashboardUiState()
    data class Loaded(val data: DashboardResponse) : DashboardUiState()
    data class Error(val message: String) : DashboardUiState()
}

class DashboardViewModel(application: Application) : AndroidViewModel(application) {

    private val dashboardRepository = DashboardRepository(application)

    private val _uiState = MutableStateFlow<DashboardUiState>(DashboardUiState.Loading)
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    fun loadDashboardData() {
        viewModelScope.launch {
            _uiState.value = DashboardUiState.Loading
            dashboardRepository.getDashboardData()
                .onSuccess { dashboardData ->
                    _uiState.value = DashboardUiState.Loaded(dashboardData)
                }
                .onFailure { throwable ->
                    _uiState.value = DashboardUiState.Error(throwable.message ?: "Erro ao carregar dashboard")
                }
        }
    }

    // Função para mapear status para cor
    fun getStatusColor(status: String): androidx.compose.ui.graphics.Color {
        return when (status.lowercase()) {
            "saudável" -> androidx.compose.ui.graphics.Color(0xFF10B981) // Verde
            "crítico" -> androidx.compose.ui.graphics.Color(0xFFEF4444) // Vermelho
            "atenção" -> androidx.compose.ui.graphics.Color(0xFFF59E0B) // Amarelo
            else -> androidx.compose.ui.graphics.Color(0xFF6B7280) // Cinza
        }
    }

    // Função para obter dados de uma categoria específica
    fun getCategoryData(categoryName: String, categoryStatuses: List<CategoryStatus>?): CategoryStatus? {
        return categoryStatuses?.find { it.category.equals(categoryName, ignoreCase = true) }
    }
}
