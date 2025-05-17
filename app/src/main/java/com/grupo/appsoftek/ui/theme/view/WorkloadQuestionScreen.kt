package com.grupo.appsoftek.ui.theme.view

import androidx.compose.runtime.Composable

import androidx.compose.ui.graphics.Color

import com.grupo.appsoftek.ui.theme.components.QuestionnaireScreen

// Data class to represent a question and its answer options
data class Question(
    val question: String,
    val options: List<String>
)

// Data class to represent the theme colors for the questionnaire
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

data class WorkloadQuestion(
    val question: String,
    val options: List<String>
)

@Composable
fun WorkloadQuestionScreen(
    onBackPressed: () -> Unit = {},
    onFinished: (List<String?>) -> Unit = {}
) {
    // Lista de perguntas de carga de trabalho
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

    // Converter de WorkloadQuestion para Question
    val questions = workloadQuestions.map {
        Question(question = it.question, options = it.options)
    }

    // Cores da Softtek para esse questionário
    val workloadTheme = QuestionnaireTheme(
        backgroundColor = Color(0xFF8DC63F),        // Verde Softtek
        questionTextColor = Color(0xFF05285E),      // Azul Softtek
        selectedOptionColor = Color(0xFF0E4C92),    // Azul mais escuro
        unselectedTextColor = Color(0xFF05285E),    // Azul Softtek para texto não selecionado
        navigationButtonColor = Color(0xFF05285E)   // Azul Softtek para botões
    )

    // Usar o componente reutilizável
    QuestionnaireScreen(
        questions = questions,
        theme = workloadTheme,
        onBackPressed = onBackPressed,
        onFinished = onFinished
    )
}