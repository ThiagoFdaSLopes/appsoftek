package com.grupo.appsoftek.ui.theme.view

import androidx.compose.runtime.Composable

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.grupo.appsoftek.ui.theme.components.QuestionaireOptionsNumeric
import com.grupo.appsoftek.ui.theme.viewmodel.QuestionResponseViewModel

// Data class to represent the theme colors for the questionnaire
data class QuestionnaireNumericTheme(
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

@Composable
fun ClimaQuestionScreen(
    onBackPressed: () -> Unit = {},
    onFinished: (List<String?>) -> Unit = {}
) {
    // ViewModel para gerenciar as respostas
    val viewModel: QuestionResponseViewModel = viewModel()
    // Lista de perguntas de carga de trabalho
    val workloadQuestions = listOf(
        WorkloadQuestion(
            "Como está o seu relacionamento com seu chefe numa escala de 1 a 5? (Sendo 01 - ruim e 05 - Ótimo)",
            listOf("1", "2", "3", "4", "5")
        ),
        WorkloadQuestion(
            "Como está o seu relacionamento com seus colegas de trabalho?\n" +
                    "(Sendo 01 - ruim e 05 - Ótimo)",
            listOf("1", "2", "3", "4", "5")
        ),
        WorkloadQuestion(
            "Sinto que sou tratado(a) com respeito pelos meus colegas de trabalho. (Sendo 01 - ruim e 05 - Ótimo)",
            listOf("1", "2", "3", "4", "5")
        ),
        WorkloadQuestion(
            "Consigo me relacionar de forma saudável e colaborativa com minha equipe. (Sendo 01 - ruim e 05 - Ótimo)",
            listOf("1", "2", "3", "4", "5")
        ),
        WorkloadQuestion(
            "Tenho liberdade para expressar minhas opiniões sem medo de retaliações. (Sendo 01 - ruim e 05 - Ótimo)",
            listOf("1", "2", "3", "4", "5")
        ),
        WorkloadQuestion(
            "Me sinto acolhido(a) a parte do time onde trabalho. (Sendo 01 - ruim e 05 - Ótimo)",
            listOf("1", "2", "3", "4", "5")
        ),
        WorkloadQuestion(
            "Sinto que existe espírito de cooperação entre os colaboradores. (Sendo 01 - ruim e 05 - Ótimo)",
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

    // Criamos uma função para salvar as respostas no banco de dados
    // Função que será passada para o onFinished do componente
    val handleFinished = { answers: List<String?> ->
        // Criar pares de pergunta e resposta
        val questionsWithAnswers = questions.mapIndexed { index, question ->
            question.question to answers.getOrNull(index)
        }

        // Salvar no banco de dados
        viewModel.saveQuestionnaireResponses("clima", questionsWithAnswers)

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