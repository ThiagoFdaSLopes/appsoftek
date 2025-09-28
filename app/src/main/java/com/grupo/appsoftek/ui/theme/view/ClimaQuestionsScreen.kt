package com.grupo.appsoftek.ui.theme.view

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.grupo.appsoftek.ui.theme.components.QuestionaireOptionsNumeric
import com.grupo.appsoftek.ui.theme.viewmodel.QuestionResponseViewModel
import com.grupo.appsoftek.ui.theme.viewmodel.QuestionsUiState
import com.grupo.appsoftek.ui.theme.viewmodel.QuestionsViewModel
import com.grupo.appsoftek.ui.theme.viewmodel.UserAssessmentsViewModel
import com.grupo.appsoftek.ui.theme.viewmodel.UserAssessmentsUiState
import com.grupo.appsoftek.data.network.AssessmentAnswerDto
import androidx.compose.ui.platform.LocalContext

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
    val questionsViewModel: QuestionsViewModel = viewModel()
    val userAssessmentsViewModel: UserAssessmentsViewModel = viewModel()
    val uiState by questionsViewModel.uiState.collectAsState()
    val userAssessmentsState by userAssessmentsViewModel.uiState.collectAsState()
    val context = LocalContext.current
    val appContext = LocalContext.current.applicationContext
    
    LaunchedEffect(Unit) { 
        questionsViewModel.loadQuestions()
        userAssessmentsViewModel.loadUserAssessments()
    }

    // Verificar se o usuário já respondeu este questionário
    LaunchedEffect(userAssessmentsState) {
        if (userAssessmentsState is UserAssessmentsUiState.Loaded) {
            val hasAnswered = userAssessmentsViewModel.hasAnsweredCategory("Clima")
            if (hasAnswered) {
                Toast.makeText(
                    context,
                    "Você já respondeu este questionário!",
                    Toast.LENGTH_LONG
                ).show()
                onBackPressed() // Voltar para a tela anterior
            }
        }
    }

    val remoteQuestions: List<WorkloadQuestion>? = when (uiState) {
        is QuestionsUiState.Loaded -> {
            val loaded = (uiState as QuestionsUiState.Loaded).questions
            val filtered = loaded.filter { it.category.equals("Clima", ignoreCase = true) }
            if (filtered.isNotEmpty()) filtered.map {
                WorkloadQuestion(
                    question = it.text,
                    options = listOf("1", "2", "3", "4", "5")
                )
            } else null
        }
        else -> null
    }

    // Converter de WorkloadQuestion para Question
    val questions = (remoteQuestions ?: emptyList()).map {
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
        val prefs = appContext.getSharedPreferences("auth_prefs", android.content.Context.MODE_PRIVATE)
        val userId = prefs.getString("current_user_uuid", null)
        val ui = uiState
        if (userId != null && ui is QuestionsUiState.Loaded) {
            val apiMap = ui.questions.filter { it.category.equals("Clima", true) }
                .map { it.text to it }.toMap()
            val payload = questions.mapIndexedNotNull { index, q ->
                val value = answers.getOrNull(index) ?: return@mapIndexedNotNull null
                val dto = apiMap[q.question] ?: return@mapIndexedNotNull null
                AssessmentAnswerDto(questionId = dto.id, category = dto.category, value = value)
            }
            questionsViewModel.submitAssessment(userId, payload) { }
        }
        onFinished(answers)
    }


    if (uiState is QuestionsUiState.Loading) {
        androidx.compose.material3.CircularProgressIndicator()
        androidx.compose.material3.Text(text = "Carregando perguntas...")
        return
    }

    if (questions.isNotEmpty()) {
        QuestionaireOptionsNumeric(
            questions = questions,
            theme = workloadTheme,
            onBackPressed = onBackPressed,
            onFinished = handleFinished
        )
    } else {
        androidx.compose.material3.Text(text = "Nenhuma pergunta disponível.", color = Color(0xFF05285E))
    }
}