package com.grupo.appsoftek.ui.theme.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.grupo.appsoftek.ui.theme.viewmodel.UserAssessmentsViewModel
import com.grupo.appsoftek.ui.theme.viewmodel.UserAssessmentsUiState

@Composable
fun QuestionnaireAccessScreen(
    category: String,
    onBackPressed: () -> Unit = {},
    content: @Composable () -> Unit
) {
    val userAssessmentsViewModel: UserAssessmentsViewModel = viewModel()
    val userAssessmentsState by userAssessmentsViewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        userAssessmentsViewModel.loadUserAssessments()
    }

    when (val state = userAssessmentsState) {
        is UserAssessmentsUiState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator()
                    Text(
                        text = "Verificando status do questionário...",
                        modifier = Modifier.padding(top = 16.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
        is UserAssessmentsUiState.Loaded -> {
            val hasAnswered = userAssessmentsViewModel.hasAnsweredCategory(category)
            
            if (hasAnswered) {
                // Mostrar mensagem de que já respondeu
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFF0F9FF)
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "✅",
                                style = MaterialTheme.typography.displayLarge
                            )
                            Text(
                                text = "Você já respondeu esse questionário",
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = Color(0xFF059669),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(top = 16.dp)
                            )
                            Text(
                                text = "Obrigado por sua participação!",
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color(0xFF6B7280),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    }
                }
            } else {
                // Permitir acesso ao questionário
                content()
            }
        }
        is UserAssessmentsUiState.Error -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Erro ao verificar status: ${state.message}",
                    color = Color.Red,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
