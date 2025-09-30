package com.grupo.appsoftek.ui.theme.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import com.grupo.appsoftek.ui.theme.components.DashboardCheckin
import com.grupo.appsoftek.ui.theme.components.DashboardHumor
import com.grupo.appsoftek.ui.theme.components.HeadDefault
import com.grupo.appsoftek.ui.theme.components.SectionAvgCard
import com.grupo.appsoftek.ui.theme.viewmodel.QuestionResponseViewModel
import com.grupo.appsoftek.ui.theme.viewmodel.DashboardViewModel
import com.grupo.appsoftek.ui.theme.viewmodel.DashboardUiState
import com.grupo.appsoftek.ui.theme.viewmodel.UserAssessmentsViewModel
import com.grupo.appsoftek.ui.theme.viewmodel.UserAssessmentsUiState
import com.grupo.appsoftek.data.network.DashboardResponse

@Composable
fun DashboardScreen(
    modifier: Modifier = Modifier,
    qrVm: QuestionResponseViewModel = viewModel(),
    dashboardVm: DashboardViewModel = viewModel(),
    userAssessmentsVm: UserAssessmentsViewModel = viewModel()
) {
    println("DEBUG: Dashboard - Função DashboardScreen chamada!")
    val totalResponses by qrVm.totalResponsesCount.collectAsState()
    val moods by qrVm.moodDaysFlow.collectAsState()
    val avgSections by qrVm.avgSectionsFlow.collectAsState()
    val dashboardUiState by dashboardVm.uiState.collectAsState()
    val userAssessmentsState by userAssessmentsVm.uiState.collectAsState()
    val scrollState = rememberScrollState()

    // Logs para debug
    println("DEBUG: Dashboard - userAssessmentsState inicial: $userAssessmentsState")

    // Carregar dados do dashboard quando a tela for criada
    LaunchedEffect(Unit) {
        println("DEBUG: Dashboard - LaunchedEffect executado")
        dashboardVm.loadDashboardData()
        userAssessmentsVm.loadUserAssessments()
    }
    
    // Recarregar dados dos assessments quando o estado mudar
    LaunchedEffect(userAssessmentsState) {
        println("DEBUG: Dashboard - LaunchedEffect userAssessmentsState mudou: $userAssessmentsState")
        if (userAssessmentsState is UserAssessmentsUiState.Error) {
            // Se houver erro, tentar recarregar
            println("DEBUG: Dashboard - Erro detectado, recarregando...")
            userAssessmentsVm.loadUserAssessments()
        }
    }

    Column {
        HeadDefault(
            title = "Dashboard",
            subtitle = "Veja como está sua evolução"
        )
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(scrollState)   // <-- aqui
                .padding(vertical = 16.dp)
        ) {
            DashboardCheckin(
                day = totalResponses
            )
            DashboardHumor(
                moods = moods
            )

            // Indicador de progresso dos questionários
            println("DEBUG: Dashboard - userAssessmentsState: $userAssessmentsState")
            println("DEBUG: Dashboard - userAssessmentsState é Loaded? ${userAssessmentsState is UserAssessmentsUiState.Loaded}")
            
            if (userAssessmentsState is UserAssessmentsUiState.Loaded) {
                println("DEBUG: Dashboard - Estado carregado, chamando getProgress()")
                val progress = userAssessmentsVm.getProgress()
                val (answered, total) = progress
                val progressPercentage = if (total > 0) (answered * 100) / total else 0
                println("DEBUG: Dashboard - Progresso calculado: $answered/$total ($progressPercentage%)")
                
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    colors = androidx.compose.material3.CardDefaults.cardColors(
                        containerColor = androidx.compose.ui.graphics.Color(0xFFF3F4F6)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Progresso dos Questionários",
                            style = androidx.compose.material3.MaterialTheme.typography.titleMedium,
                            color = androidx.compose.ui.graphics.Color(0xFF1E3A8A)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "$answered de $total questionários respondidos ($progressPercentage%)",
                            style = androidx.compose.material3.MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        androidx.compose.material3.LinearProgressIndicator(
                            progress = progressPercentage / 100f,
                            modifier = Modifier.fillMaxWidth(),
                            color = androidx.compose.ui.graphics.Color(0xFF10B981),
                            trackColor = androidx.compose.ui.graphics.Color(0xFFE5E7EB)
                        )
                    }
                }
            }

            // Grid 2x2 dos cards de média
            when (val state = dashboardUiState) {
                is DashboardUiState.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator()
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Carregando dados do dashboard...")
                        }
                    }
                }
                is DashboardUiState.Loaded -> {
                    val dashboardData = state.data
                    val categoryStatuses = dashboardData.categoryStatuses ?: emptyList()
                    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            // Card 1: Produtividade
                            val produtividadeData = dashboardVm.getCategoryData("Produtividade", categoryStatuses)
                            SectionAvgCard(
                                modifier = Modifier.weight(1f),
                                title = "Produtividade",
                                average = produtividadeData?.percentage?.toDouble() ?: 0.0,
                                label = produtividadeData?.status ?: "N/A",
                                color = produtividadeData?.let { dashboardVm.getStatusColor(it.status) } ?: Color.Gray
                            )
                            
                            // Card 2: Carga de trabalho
                            val cargaTrabalhoData = dashboardVm.getCategoryData("Carga de trabalho", categoryStatuses)
                            SectionAvgCard(
                                modifier = Modifier.weight(1f),
                                title = "Carga de Trabalho",
                                average = cargaTrabalhoData?.percentage?.toDouble() ?: 0.0,
                                label = cargaTrabalhoData?.status ?: "N/A",
                                color = cargaTrabalhoData?.let { dashboardVm.getStatusColor(it.status) } ?: Color.Gray
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            // Card 3: Clima
                            val climaData = dashboardVm.getCategoryData("Clima", categoryStatuses)
                            SectionAvgCard(
                                modifier = Modifier.weight(1f),
                                title = "Clima",
                                average = climaData?.percentage?.toDouble() ?: 0.0,
                                label = climaData?.status ?: "N/A",
                                color = climaData?.let { dashboardVm.getStatusColor(it.status) } ?: Color.Gray
                            )
                            
                            // Card 4: Liderança
                            val liderancaData = dashboardVm.getCategoryData("Liderança", categoryStatuses)
                            SectionAvgCard(
                                modifier = Modifier.weight(1f),
                                title = "Liderança",
                                average = liderancaData?.percentage?.toDouble() ?: 0.0,
                                label = liderancaData?.status ?: "N/A",
                                color = liderancaData?.let { dashboardVm.getStatusColor(it.status) } ?: Color.Gray
                            )

                            // Card 5: Comunicação
                            val comunicacaoData = dashboardVm.getCategoryData("Comunicação", categoryStatuses)
                            SectionAvgCard(
                                modifier = Modifier.weight(1f),
                                title = "Comunicação",
                                average = comunicacaoData?.percentage?.toDouble() ?: 0.0,
                                label = comunicacaoData?.status ?: "N/A",
                                color = comunicacaoData?.let { dashboardVm.getStatusColor(it.status) } ?: Color.Gray
                            )
                        }
                    }
                }
                is DashboardUiState.Error -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Erro: ${state.message}",
                            color = Color.Red
                        )
                    }
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun DashboardScreenPreview() {
    DashboardScreen()
}