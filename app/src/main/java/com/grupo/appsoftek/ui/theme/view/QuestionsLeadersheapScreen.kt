package com.grupo.appsoftek.ui.theme.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.grupo.appsoftek.ui.theme.components.QuestionaireOptionsNumeric
import com.grupo.appsoftek.ui.theme.viewmodel.QuestionResponseViewModel

@Composable
fun QuestionsLeadersheapScreen(
    onBackPressed: () -> Unit = {},
    onFinished: (List<String?>) -> Unit = {}
) {
    // ViewModel para gerenciar as respostas
    val viewModel: QuestionResponseViewModel = viewModel()
    // Lista de perguntas de carga de trabalho
    val workloadQuestions = listOf(
        WorkloadQuestion(
            "Minha liderança demonstra interesse pelo meu bem-estar no trabalho\n" +
                    "(Sendo 01 - ruim e 05 - Ótimo)",
            listOf("1", "2", "3", "4", "5")
        ),
        WorkloadQuestion(
            "Minha liderança está disponível para me ouvir quando necessário.\n" +
                    "(Sendo 01 - ruim e 05 - Ótimo)",
            listOf("1", "2", "3", "4", "5")
        ),
        WorkloadQuestion(
            "Me sinto confortável para reportar problemas ou dificuldades ao meu líder. (Sendo 01 - ruim e 05 - Ótimo)",
            listOf("1", "2", "3", "4", "5")
        ),
        WorkloadQuestion(
            "Minha liderança reconhece minhas entregas e esforços. (Sendo 01 - ruim e 05 - Ótimo)",
            listOf("1", "2", "3", "4", "5")
        ),
        WorkloadQuestion(
            "Existe confiança e transparência na relação com minha liderança. (Sendo 01 - ruim e 05 - Ótimo)",
            listOf("1", "2", "3", "4", "5")
        )
    )

    // Converter de WorkloadQuestion para Question
    val questions = workloadQuestions.map {
        Question(question = it.question, options = it.options)
    }

    // Cores da Softtek para esse questionário
    val workloadTheme = QuestionnaireNumericTheme(
        backgroundColor = Color(0xFF8DC63F),        // Verde Softtek
        questionTextColor = Color(0xFF05285E),      // Azul Softtek
        selectedOptionColor = Color(0xFF0E4C92),    // Azul mais escuro
        unselectedTextColor = Color(0xFF05285E),    // Azul Softtek para texto não selecionado
        navigationButtonColor = Color(0xFF05285E)   // Azul Softtek para botões
    )

    val handleFinished = { answers: List<String?> ->
        // Criar pares de pergunta e resposta
        val questionsWithAnswers = questions.mapIndexed { index, question ->
            question.question to answers.getOrNull(index)
        }

        // Salvar no banco de dados
        viewModel.saveQuestionnaireResponses("liderança", questionsWithAnswers)

        // Chamar a função original onFinished
        onFinished(answers)
    }

    // Usar o componente reutilizável
    QuestionaireOptionsNumeric(
        questions = questions,
        theme = workloadTheme,
        onBackPressed = onBackPressed,
        onFinished = handleFinished
    )
}