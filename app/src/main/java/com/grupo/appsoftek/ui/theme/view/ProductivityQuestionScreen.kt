package com.grupo.appsoftek.ui.theme.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.grupo.appsoftek.ui.theme.components.QuestionnaireScreen

// Data class para representar uma pergunta de saúde mental
data class MentalHealthQuestion(
    val question: String,
    val options: List<String>
)

@Composable
fun ProductivityQuestionScreen(
    onBackPressed: () -> Unit = {},
    onFinished: (List<String?>) -> Unit = {}
) {
    // Lista de perguntas sobre saúde mental
    val mentalHealthQuestions = listOf(
        MentalHealthQuestion(
            "Você tem apresentado sintomas como insônia, irritabilidade ou cansaço extremo?",
            listOf("Nunca", "Raramente", "Às vezes", "Frequentemente", "Sempre")
        ),
        MentalHealthQuestion(
            "Você sente que sua saúde mental prejudica sua produtividade no trabalho?",
            listOf("Nunca", "Raramente", "Às vezes", "Frequentemente", "Sempre")
        )
    )

    // Converter de MentalHealthQuestion para Question
    val questions = mentalHealthQuestions.map {
        Question(question = it.question, options = it.options)
    }

    // Tema baseado no design mostrado na imagem
    val mentalHealthTheme = QuestionnaireTheme(
        backgroundColor = Color(0xFF002A5C),           // Azul escuro do fundo
        cardBackgroundColor = Color.White,             // Card branco
        questionTextColor = Color(0xFF8BB82D),         // Verde para texto da pergunta
        selectedOptionColor = Color(0xFF8BB82D),       // Verde para opção selecionada (Raramente na imagem)
        unselectedOptionColor = Color.White,           // Branco para opções não selecionadas
        selectedTextColor = Color.White,               // Texto branco na opção selecionada
        unselectedTextColor = Color(0xFF8BB82D),       // Texto verde nas opções não selecionadas
        borderColor = Color(0xFFD0D0D0),               // Borda cinza claro para as opções
        navigationButtonColor = Color(0xFF8BB82D)      // Verde para botões de navegação
    )

    // Usar o componente reutilizável
    QuestionnaireScreen(
        questions = questions,
        theme = mentalHealthTheme,
        onBackPressed = onBackPressed,
        onFinished = onFinished
    )
}