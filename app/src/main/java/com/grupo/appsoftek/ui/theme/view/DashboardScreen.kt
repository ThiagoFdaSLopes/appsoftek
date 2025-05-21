package com.grupo.appsoftek.ui.theme.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.grupo.appsoftek.ui.theme.components.DashboardCheckin
import com.grupo.appsoftek.ui.theme.components.DashboardHumor
import com.grupo.appsoftek.ui.theme.components.HeadDefault
import com.grupo.appsoftek.ui.theme.components.SectionAvgCard
import com.grupo.appsoftek.ui.theme.viewmodel.QuestionResponseViewModel

@Composable
fun DashboardScreen(
    modifier: Modifier = Modifier,
    qrVm: QuestionResponseViewModel = viewModel()
) {
    val totalResponses by qrVm.totalResponsesCount.collectAsState()
    val moods by qrVm.moodDaysFlow.collectAsState()
    val avgSections by qrVm.avgSectionsFlow.collectAsState()
    val scrollState = rememberScrollState()

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

            // Grid 2x2 dos cards de média
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    SectionAvgCard(
                        modifier = Modifier.weight(1f),
                        title = avgSections.getOrNull(0)?.title.orEmpty(),
                        average = avgSections.getOrNull(0)?.average ?: 0.0,
                        label = avgSections.getOrNull(0)?.label.orEmpty(),
                        color = avgSections.getOrNull(0)?.color ?: Color.Gray
                    )
                    SectionAvgCard(
                        modifier = Modifier.weight(1f),
                        title = avgSections.getOrNull(1)?.title.orEmpty(),
                        average = avgSections.getOrNull(1)?.average ?: 0.0,
                        label = avgSections.getOrNull(1)?.label.orEmpty(),
                        color = avgSections.getOrNull(1)?.color ?: Color.Gray
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    SectionAvgCard(
                        modifier = Modifier.weight(1f),
                        title = avgSections.getOrNull(2)?.title.orEmpty(),
                        average = avgSections.getOrNull(2)?.average ?: 0.0,
                        label = avgSections.getOrNull(2)?.label.orEmpty(),
                        color = avgSections.getOrNull(2)?.color ?: Color.Gray
                    )
                    SectionAvgCard(
                        modifier = Modifier.weight(1f),
                        title = avgSections.getOrNull(3)?.title.orEmpty(),
                        average = avgSections.getOrNull(3)?.average ?: 0.0,
                        label = avgSections.getOrNull(3)?.label.orEmpty(),
                        color = avgSections.getOrNull(3)?.color ?: Color.Gray
                    )
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