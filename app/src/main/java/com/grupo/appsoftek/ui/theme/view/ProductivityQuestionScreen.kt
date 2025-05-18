package com.grupo.appsoftek.ui.theme.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.grupo.appsoftek.ui.theme.components.QuestionnaireScreen
import com.grupo.appsoftek.ui.theme.viewmodel.QuestionResponseViewModel

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
    // ViewModel para gerenciar as respostas
    val viewModel: QuestionResponseViewModel = viewModel()
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

    // Criamos uma função para salvar as respostas no banco de dados
    // Função que será passada para o onFinished do componente
    val handleFinished = { answers: List<String?> ->
        // Criar pares de pergunta e resposta
        val questionsWithAnswers = questions.mapIndexed { index, question ->
            question.question to answers.getOrNull(index)
        }

        // Salvar no banco de dados
        viewModel.saveQuestionnaireResponses("produtividade", questionsWithAnswers)

        // Chamar a função original onFinished
        onFinished(answers)
    }

    // Usar o componente reutilizável
    QuestionnaireScreen(
        questions = questions,
        theme = mentalHealthTheme,
        onBackPressed = onBackPressed,
        onFinished = handleFinished
    )
}