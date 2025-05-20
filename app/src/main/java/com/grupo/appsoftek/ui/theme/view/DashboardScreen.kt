package com.grupo.appsoftek.ui.theme.view

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.grupo.appsoftek.R
import com.grupo.appsoftek.ui.theme.components.DailyProgress
import com.grupo.appsoftek.ui.theme.components.DashboardCheckin
import com.grupo.appsoftek.ui.theme.components.DashboardHumor
import com.grupo.appsoftek.ui.theme.components.HeadDefault
import com.grupo.appsoftek.ui.theme.components.MoodDay
import com.grupo.appsoftek.ui.theme.components.ProgressBarChart
import com.grupo.appsoftek.ui.theme.viewmodel.QuestionResponseViewModel

@Composable
fun DashboardScreen(
    modifier: Modifier = Modifier,
    qrVm: QuestionResponseViewModel = viewModel()
) {
    val totalResponses by qrVm.totalResponsesCount.collectAsState()


    // Exemplo de lista de humores (substitua pela sua lógica real)
    val userMoods = listOf(
        MoodDay("10/05", R.drawable.icon_fear),
        MoodDay("11/05", R.drawable.icon_exausto),
        MoodDay("13/05", R.drawable.icon_angry),
        MoodDay("14/05", R.drawable.icon_happy),
        MoodDay("15/05", R.drawable.icon_anxious),
        MoodDay("16/05", R.drawable.icon_happy),
        MoodDay("17/05", R.drawable.icon_happy)
    )
    val dailyProgressListSample = listOf(
        DailyProgress(date = "10/05", progressPercentage = 0.6f),
        DailyProgress(date = "11/05", progressPercentage = 0.8f),
        DailyProgress(date = "12/05", progressPercentage = 0.4f),
        DailyProgress(date = "13/05", progressPercentage = 0.9f),
        DailyProgress(date = "14/05", progressPercentage = 0.7f)
    )

    Column {
        HeadDefault(
            title = "Dashboard",
            subtitle = "Veja como está sua evolução"
        )
        DashboardCheckin(
            day = totalResponses // Supondo que o 'day' seja fixo ou obtido de outra forma
        )
        DashboardHumor(
            moods = userMoods // Passa a lista de humores para o componente
        )
        ProgressBarChart(
            overallProgress = com.grupo.appsoftek.ui.theme.components.OverallProgress(
                progressPercentage = 0.75f,
                completedEvaluations = 15,
                totalEvaluations = 20
            ),
            dailyProgressList = dailyProgressListSample

        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun DashboardScreenPreview() {
    DashboardScreen()
}