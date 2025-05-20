package com.grupo.appsoftek.ui.theme.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Modelo de dados para representar uma entrada de progresso diário
 */
data class DailyProgress(
    val date: String,  // Formato "dd/MM"
    val progressPercentage: Float,  // Valor entre 0f e 1f
    val completedEvaluations: Int = 0,
    val totalEvaluations: Int = 0
)

/**
 * Modelo de dados para o progresso geral
 */
data class OverallProgress(
    val progressPercentage: Float,  // Valor entre 0f e 1f
    val completedEvaluations: Int,
    val totalEvaluations: Int
)

/**
 * Componente principal do gráfico de progresso
 */
@Composable
fun ProgressBarChart(
    overallProgress: OverallProgress,
    dailyProgressList: List<DailyProgress>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            // Título
            Text(
                text = "Evolução geral",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF002766)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Progresso geral
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Progresso total",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF002766)
                )

                Text(
                    text = "${(overallProgress.progressPercentage * 100).toInt()}%",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF7CB342)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Barra de progresso
            LinearProgressIndicator(
                progress = overallProgress.progressPercentage,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
                    .clip(RoundedCornerShape(6.dp)),
                color = Color(0xFF7CB342),
                trackColor = Color(0xFFE0E0E0)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Texto de avaliações
            Text(
                text = "${overallProgress.completedEvaluations} de ${overallProgress.totalEvaluations} avaliações concluídas",
                fontSize = 14.sp,
                color = Color(0xFF666666),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Gráfico de barras
            DailyProgressBarChart(
                dailyProgressList = dailyProgressList,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ProgressBarChartPreview() {
    val overallProgressSample = OverallProgress(progressPercentage = 0.75f, completedEvaluations = 15, totalEvaluations = 20)
    val dailyProgressListSample = listOf(
        DailyProgress(date = "10/05", progressPercentage = 0.6f),
        DailyProgress(date = "11/05", progressPercentage = 0.8f),
        DailyProgress(date = "12/05", progressPercentage = 0.4f),
        DailyProgress(date = "13/05", progressPercentage = 0.9f),
        DailyProgress(date = "14/05", progressPercentage = 0.7f)
    )
    ProgressBarChart(overallProgress = overallProgressSample, dailyProgressList = dailyProgressListSample)
}

/**
 * Componente do gráfico de barras diário
 */
@Composable
fun DailyProgressBarChart(
    dailyProgressList: List<DailyProgress>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        dailyProgressList.forEach { progress ->
            DailyBar(
                date = progress.date,
                progressPercentage = progress.progressPercentage,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DailyProgressBarChartPreview() {
    val dailyProgressListSample = listOf(
        DailyProgress(date = "10/05", progressPercentage = 0.6f),
        DailyProgress(date = "11/05", progressPercentage = 0.8f),
        DailyProgress(date = "12/05", progressPercentage = 0.4f),
        DailyProgress(date = "13/05", progressPercentage = 0.9f),
        DailyProgress(date = "14/05", progressPercentage = 0.7f)
    )
    DailyProgressBarChart(dailyProgressList = dailyProgressListSample)
}

/**
 * Componente de barra individual
 */
@Composable
fun DailyBar(
    date: String,
    progressPercentage: Float,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(horizontal = 4.dp)
    ) {
        // Barra
        Box(
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .fillMaxHeight(progressPercentage)
                .background(
                    color = Color(0xFF7CB342),
                    shape = RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)
                )
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Data
        Text(
            text = date,
            fontSize = 12.sp,
            color = Color(0xFF666666),
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DailyBarPreview() {
    DailyBar(date = "15/05", progressPercentage = 0.5f)
}

@Preview(showBackground = true, name = "DailyBar - Progresso Alto")
@Composable
private fun DailyBarPreviewHighProgress() {
    DailyBar(date = "16/05", progressPercentage = 0.9f)
}

@Preview(showBackground = true, name = "DailyBar - Progresso Baixo")
@Composable
private fun DailyBarPreviewLowProgress() {
    DailyBar(date = "17/05", progressPercentage = 0.2f)
}