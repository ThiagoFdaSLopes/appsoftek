package com.grupo.appsoftek.ui.theme.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.grupo.appsoftek.ui.theme.components.QuestionnaireScreen
import com.grupo.appsoftek.ui.theme.viewmodel.QuestionResponseViewModel
import com.grupo.appsoftek.ui.viewmodel.QuoteViewModel

// Data class para representar uma pergunta genérica
data class Question(
    val question: String,
    val options: List<String>
)

// Data class para tema do questionário
data class QuestionnaireTheme(
    val backgroundColor: Color,
    val cardBackgroundColor: Color = Color.White,
    val questionTextColor: Color,
    val selectedOptionColor: Color,
    val unselectedOptionColor: Color = Color.White,
    val selectedTextColor: Color = Color.White,
    val unselectedTextColor: Color,
    val borderColor: Color = Color(0xFFD0D0D0),
    val navigationButtonColor: Color,
    val navigationButtonTextColor: Color = Color.White
)

// Data class específica para carga de trabalho
data class WorkloadQuestion(
    val question: String,
    val options: List<String>
)

@Composable
fun WorkloadQuestionScreen(
    onBackPressed: () -> Unit = {},
    onFinished: (List<String?>) -> Unit = {}
) {
    // ViewModel para gerenciar as respostas
    val viewModel: QuestionResponseViewModel = viewModel()
    // ViewModel para buscar citação
    val quoteViewModel: QuoteViewModel = viewModel()
    // Observando LiveData com Compose
    val quote by quoteViewModel.quote.observeAsState()
    val isLoading by quoteViewModel.isLoading.observeAsState(false)
    val error by quoteViewModel.error.observeAsState()

    // Buscando uma citação assim que o Composable entra em composição
    LaunchedEffect(Unit) {
        quoteViewModel.fetchRandomQuote()
    }

    // Perguntas de carga de trabalho
    val workloadQuestions = listOf(
        WorkloadQuestion(
            "Como você avalia a sua carga de trabalho?",
            listOf("Muito Leve", "Leve", "Média", "Alta", "Muito Alta")
        ),
        WorkloadQuestion(
            "Sua carga de trabalho afeta sua qualidade de vida?",
            listOf("Não", "Raramente", "Às vezes", "Frequentemente", "Sempre")
        ),
        WorkloadQuestion(
            "Você trabalha além do seu horário regular?",
            listOf("Não", "Raramente", "Às vezes", "Frequentemente", "Sempre")
        )
    )
    // Converter para Question genérica
    val questions = workloadQuestions.map {
        Question(question = it.question, options = it.options)
    }

    // Tema Softtek
    val workloadTheme = QuestionnaireTheme(
        backgroundColor = Color(0xFF8DC63F),
        questionTextColor = Color(0xFF05285E),
        selectedOptionColor = Color(0xFF0E4C92),
        unselectedTextColor = Color(0xFF05285E),
        navigationButtonColor = Color(0xFF05285E)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Card da citação
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = CardDefaults.cardColors(containerColor = workloadTheme.cardBackgroundColor),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                when {
                    isLoading -> {
                        CircularProgressIndicator()
                    }
                    error != null -> {
                        Text(
                            text = error ?: "Erro ao carregar citação",
                            style = MaterialTheme.typography.bodyMedium,
                            color = workloadTheme.questionTextColor,
                            textAlign = TextAlign.Center
                        )
                    }
                    quote != null -> {
                        Text(
                            text = "“${quote!!.quote}” — ${quote!!.author}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = workloadTheme.questionTextColor,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

        val handleFinished = { answers: List<String?> ->
            // Criar pares de pergunta e resposta
            val questionsWithAnswers = questions.mapIndexed { index, question ->
                question.question to answers.getOrNull(index)
            }

            // Salvar no banco de dados
            viewModel.saveQuestionnaireResponses("carga-de-trabalho", questionsWithAnswers)

            // Chamar a função original onFinished
            onFinished(answers)
        }

        // Tela de questionário reutilizável
        QuestionnaireScreen(
            questions = questions,
            theme = workloadTheme,
            onBackPressed = onBackPressed,
            onFinished = handleFinished
        )
    }
}
