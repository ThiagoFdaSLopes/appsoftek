package com.grupo.appsoftek.ui.theme.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.grupo.appsoftek.ui.theme.components.QuestionaireOptionsNumeric

@Composable
fun QuestionsComunicationScreen(
    onBackPressed: () -> Unit = {},
    onFinished: (List<String?>) -> Unit = {}
) {
    // Lista de perguntas de comunicação
    val climaQuestions = listOf(
        WorkloadQuestion(
            "Recebo orientações claras e objetivas sobre minhas atividades e responsabilidades.\n" +
                    "(Sendo 01 - ruim e 05 - Ótimo)",
            listOf("1", "2", "3", "4", "5")
        ),
        WorkloadQuestion(
            "Sinto que posso me comunicar abertamente com minha liderança.\n" +
                    "(Sendo 01 - ruim e 05 - Ótimo)",
            listOf("1", "2", "3", "4", "5")
        ),
        WorkloadQuestion(
            "As informações importantes circulam de forma eficiente dentro da empresa.. (Sendo 01 - ruim e 05 - Ótimo)",
            listOf("1", "2", "3", "4", "5")
        ),
        WorkloadQuestion(
            " Tenho clareza sobre as metas e os resultados esperados de mim.. (Sendo 01 - ruim e 05 - Ótimo)",
            listOf("1", "2", "3", "4", "5")
        )
    )

    // Converter de WorkloadQuestion para Question
    val questions = climaQuestions.map {
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

    // Usar o componente reutilizável
    QuestionaireOptionsNumeric(
        questions = questions,
        theme = workloadTheme,
        onBackPressed = onBackPressed,
        onFinished = onFinished
    )
}