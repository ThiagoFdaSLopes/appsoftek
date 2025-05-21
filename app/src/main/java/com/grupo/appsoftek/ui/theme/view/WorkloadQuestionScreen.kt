package com.grupo.appsoftek.ui.theme.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.grupo.appsoftek.ui.theme.components.QuestionnaireScreen
import com.grupo.appsoftek.ui.theme.viewmodel.QuestionResponseViewModel

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
    ) {


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
