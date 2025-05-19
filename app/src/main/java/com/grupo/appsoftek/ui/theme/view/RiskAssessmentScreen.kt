package com.grupo.appsoftek.ui.theme.view

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.grupo.appsoftek.R

data class Section(
    val title: String,
    @DrawableRes val icon: Int,
    val answered: Int,
    val total: Int,
    val cardColor: Color,
    val progressColor: Color
)

@Composable
fun RiskAssessmentScreen(sections: List<Section>, onSectionClick: (String) -> Unit = {}) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // ── Header ───────────────────────────────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF1E3A8A))
                .padding(horizontal = 16.dp, vertical = 24.dp)
        ) {
            Column {
                Text(
                    text = "Avaliação de Riscos Psicossociais",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = Color.White
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Complete as seções para visualizar seu resultado",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFFBBDEFB)
                )
                Spacer(Modifier.height(12.dp))
                val totalAnswered  = sections.sumOf { it.answered }
                val totalQuestions = sections.sumOf { it.total }
                LinearProgressIndicator(
                    progress   = if (totalQuestions>0) totalAnswered / totalQuestions.toFloat() else 0f,
                    modifier    = Modifier
                        .fillMaxWidth()
                        .height(4.dp),
                    color       = Color(0xFF64FFDA),
                    trackColor  = Color(0xFF3F51B5)
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        // ── Lista de Seções ────────────────────────────────────────────────────
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(sections) { section ->
                SectionCard(
                    section = section,
                    onClick = { onSectionClick(section.title) }
                )
            }
        }
    }
}

@Composable
private fun SectionCard(
    section: Section,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = section.cardColor),
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .clickable(onClick = onClick) // Adicionando clickable
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxHeight() // Faz o Row ocupar toda a altura do Card
                .padding(horizontal = 16.dp)
        ) {
            // Ícone dentro de círculo branco
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(section.icon),
                    contentDescription = section.title,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = section.title,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                    color = Color.White
                )
                Text(
                    text = "${section.total} Questões",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.9f)
                )
            }

            // Indicador circular de progresso + texto 1/2
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(48.dp)
            ) {
                val progress = section.answered / section.total.toFloat()
                CircularProgressIndicator(
                    progress = progress,
                    strokeWidth = 4.dp,
                    modifier = Modifier.matchParentSize(),
                    color = section.progressColor
                )
                Text(
                    text = "${section.answered}/${section.total}",
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                    color = Color.White
                )
            }
        }
    }
}