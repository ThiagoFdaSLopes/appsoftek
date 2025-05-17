package com.grupo.appsoftek.ui.theme.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.grupo.appsoftek.ui.theme.components.ActionButtons
import com.grupo.appsoftek.ui.theme.components.MoodSelectionCard
import com.grupo.appsoftek.ui.theme.viewmodel.MoodTrackingViewModel

@Composable
fun MoodTrackingScreen(
    viewModel: MoodTrackingViewModel = viewModel(),
    onBackPressed: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF8DC63F)) // Verde Softtek
    ) {
        // Ícone de sino no canto superior direito
        Icon(
            imageVector = Icons.Default.Notifications,
            contentDescription = "Notificações",
            tint = Color(0xFF05285E), // Azul Softtek
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
                .size(28.dp)
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Primeira pergunta - Emoji de hoje
            MoodSelectionCard(
                title = "Escolha o seu emoji de hoje!",
                options = viewModel.todayEmojis,
                selectedId = viewModel.selectedEmojiId,
                onOptionSelected = { viewModel.selectEmoji(it) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Segunda pergunta - Como se sente
            MoodSelectionCard(
                title = "Como você se sente hoje?",
                options = viewModel.feelingOptions,
                selectedId = viewModel.selectedFeelingId,
                onOptionSelected = { viewModel.selectFeeling(it) }
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Botões de ação
            ActionButtons(
                onBack = onBackPressed,
                onSubmit = { viewModel.saveResponses() },
                isSubmitEnabled = viewModel.isFormComplete
            )
        }
    }
}