package com.grupo.appsoftek.ui.theme.viewmodel

import android.app.Application
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.grupo.appsoftek.data.network.CategoryStatus
import com.grupo.appsoftek.data.network.NotificationItem
import com.grupo.appsoftek.data.repository.DashboardRepository
import com.grupo.appsoftek.data.repository.NotificationsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class UiNotificationItem(
    val id: String,
    val type: String,
    val title: String,
    val message: String,
    val color: Color,
    val affectedCategories: List<String>
)

sealed class NotificationsUiState {
    data object Loading : NotificationsUiState()
    data class Loaded(val items: List<UiNotificationItem>) : NotificationsUiState()
    data class Error(val message: String) : NotificationsUiState()
}

class NotificationsViewModel(application: Application) : AndroidViewModel(application) {
    private val notificationsRepository = NotificationsRepository(application)
    private val dashboardRepository = DashboardRepository(application)

    private val _uiState = MutableStateFlow<NotificationsUiState>(NotificationsUiState.Loading)
    val uiState: StateFlow<NotificationsUiState> = _uiState

    fun load() {
        viewModelScope.launch {
            _uiState.value = NotificationsUiState.Loading
            val notifResult = notificationsRepository.getNotifications()
            val dashResult = dashboardRepository.getDashboardData()

            val notifications = notifResult.getOrElse { emptyList() }
            val categoryStatuses = dashResult.getOrNull()?.categoryStatuses ?: emptyList()

            val filtered = filterByDashboardStatus(notifications, categoryStatuses)
            val mapped = mapToUi(filtered, categoryStatuses)
            val fallback = mapToUi(notifications, categoryStatuses)

            if (mapped.isEmpty() && notifications.isNotEmpty()) {
                _uiState.value = NotificationsUiState.Loaded(fallback)
            } else if (notifResult.isSuccess) {
                _uiState.value = NotificationsUiState.Loaded(sortBySeverity(mapped))
            } else {
                _uiState.value = NotificationsUiState.Error(notifResult.exceptionOrNull()?.message ?: "Erro ao carregar notificações")
            }
        }
    }

    private fun filterByDashboardStatus(
        notifications: List<NotificationItem>,
        statuses: List<CategoryStatus>
    ): List<NotificationItem> {
        val statusTypes = statuses.map { it.status }.toSet()
        return notifications.filter { it.type.equals("support", ignoreCase = true) || statusTypes.contains(it.type) }
    }

    private fun mapToUi(
        notifications: List<NotificationItem>,
        statuses: List<CategoryStatus>
    ): List<UiNotificationItem> {
        val statusToCategories: Map<String, List<String>> = statuses
            .groupBy { it.status }
            .mapValues { entry -> entry.value.map { it.category } }

        return notifications.map { n ->
            val affected = if (n.type.equals("support", ignoreCase = true)) emptyList() else statusToCategories[n.type].orEmpty()
            UiNotificationItem(
                id = n.id,
                type = n.type,
                title = enrichTitleWithArea(n.title, n.type, affected),
                message = n.message,
                color = colorForType(n.type),
                affectedCategories = affected
            )
        }
    }

    private fun enrichTitleWithArea(title: String, type: String, areas: List<String>): String {
        if (areas.isEmpty()) return title
        val joined = areas.joinToString(", ")
        return when (type) {
            "Crítico" -> "$title · Áreas críticas: $joined"
            "Atenção" -> "$title · Áreas em atenção: $joined"
            "Moderado" -> "$title · Áreas moderadas: $joined"
            "Estável" -> "$title · Áreas estáveis: $joined"
            "Saudável" -> "$title · Áreas saudáveis: $joined"
            else -> "$title · Áreas: $joined"
        }
    }

    private fun colorForType(type: String): Color = when (type) {
        "Crítico" -> Color(0xFFD32F2F)
        "Atenção" -> Color(0xFFFFA000)
        "Moderado" -> Color(0xFFFFC107)
        "Estável" -> Color(0xFF66BB6A)
        "Saudável" -> Color(0xFF2E7D32)
        "N/A" -> Color(0xFF9E9E9E)
        "support", "Support" -> Color(0xFF0A66C2)
        else -> Color(0xFF607D8B)
    }

    private fun sortBySeverity(items: List<UiNotificationItem>): List<UiNotificationItem> {
        val rank = mapOf(
            "Crítico" to 0,
            "Atenção" to 1,
            "Moderado" to 2,
            "Estável" to 3,
            "Saudável" to 4,
            "N/A" to 5,
            "support" to 6
        )
        return items.sortedWith(compareBy({ rank[it.type] ?: 99 }, { it.title }))
    }
}


