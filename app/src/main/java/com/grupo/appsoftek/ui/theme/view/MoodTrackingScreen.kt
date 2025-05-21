package com.grupo.appsoftek.ui.theme.view

import android.app.Application
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.grupo.appsoftek.factory.MoodTrackingViewModelFactory
import com.grupo.appsoftek.ui.theme.components.ActionButtons
import com.grupo.appsoftek.ui.theme.components.MoodSelectionCard
import com.grupo.appsoftek.ui.theme.viewmodel.MoodTrackingViewModel

@Composable
fun MoodTrackingScreen(
    navController: NavController? = null,
    onBackPressed: () -> Unit = {},
    onFinished: () -> Unit = {}
) {
    val context = LocalContext.current
    // Criar o factory
    val factory = MoodTrackingViewModelFactory(context.applicationContext as Application)
    // Usar o factory para criar o ViewModel
    val viewModel: MoodTrackingViewModel = viewModel(factory = factory)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF8DC63F)) // Verde Softtek
    ) {

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

            // Botões de ação
            ActionButtons(
                onBack = onBackPressed,
                onSubmit = {
                    // Salvar as respostas e navegar para a próxima tela
                    viewModel.saveResponses()
                    navController?.popBackStack()
                    Toast.makeText(
                        navController?.context,
                        "Respostas sobre bem-estar emocional salvas",
                        Toast.LENGTH_SHORT
                    ).show()
                },
                isSubmitEnabled = viewModel.isFormComplete
            )
        }
    }
}