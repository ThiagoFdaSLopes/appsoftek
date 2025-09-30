package com.grupo.appsoftek.ui.theme.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.background
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.grupo.appsoftek.ui.theme.components.HeadTitle
import com.grupo.appsoftek.ui.theme.viewmodel.NotificationsUiState
import com.grupo.appsoftek.ui.theme.viewmodel.NotificationsViewModel
import com.grupo.appsoftek.ui.theme.viewmodel.UiNotificationItem
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun NotificationsScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    val vm: NotificationsViewModel = viewModel()
    val uiState by vm.uiState.collectAsState()

    LaunchedEffect(Unit) {
        vm.load()
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HeadTitle(modifier = Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.height(12.dp))

        when (val state = uiState) {
            is NotificationsUiState.Loading -> {
                CircularProgressIndicator()
            }
            is NotificationsUiState.Error -> {
                Text(text = state.message, color = Color.Red, modifier = Modifier.padding(16.dp))
            }
            is NotificationsUiState.Loaded -> {
                LazyColumn(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    items(state.items) { item ->
                        NotificationCard(item)
                    }
                }
            }
        }
    }
}

@Composable
private fun NotificationCard(item: UiNotificationItem) {
    Card(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        // Faixa colorida por status
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .background(item.color)
        )
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = item.type, style = MaterialTheme.typography.labelSmall, color = item.color)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = item.title, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = item.message, style = MaterialTheme.typography.bodyMedium)
            if (item.affectedCategories.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Áreas: " + item.affectedCategories.joinToString(", "),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF6B7280)
                )
            }
        }
    }
}