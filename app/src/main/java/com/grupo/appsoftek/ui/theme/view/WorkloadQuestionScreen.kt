package com.grupo.appsoftek.ui.theme.view

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.ui.unit.dp
import com.grupo.appsoftek.ui.theme.components.QuestionnaireScreen
import com.grupo.appsoftek.ui.theme.viewmodel.QuestionResponseViewModel
import com.grupo.appsoftek.ui.theme.viewmodel.QuestionsUiState
import com.grupo.appsoftek.ui.theme.viewmodel.QuestionsViewModel
import com.grupo.appsoftek.ui.theme.viewmodel.UserAssessmentsViewModel
import com.grupo.appsoftek.ui.theme.viewmodel.UserAssessmentsUiState
import com.grupo.appsoftek.data.network.AssessmentAnswerDto
import androidx.compose.ui.platform.LocalContext

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
            val hasAnswered = userAssessmentsViewModel.hasAnsweredCategory("Carga de trabalho")
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

    val remoteWorkloadQuestions: List<WorkloadQuestion>? = when (uiState) {
        is QuestionsUiState.Loaded -> {
            val loaded = (uiState as QuestionsUiState.Loaded).questions
            val filtered = loaded.filter { it.category.equals("Carga de trabalho", ignoreCase = true) }
            if (filtered.isNotEmpty()) filtered.map {
                WorkloadQuestion(
                    question = it.text,
                    options = listOf("Não", "Raramente", "Às vezes", "Frequentemente", "Sempre")
                )
            } else null
        }
        else -> null
    }
    // Converter para Question genérica
    val questions = (remoteWorkloadQuestions ?: emptyList()).map {
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
            val prefs = appContext.getSharedPreferences("auth_prefs", android.content.Context.MODE_PRIVATE)
            val userId = prefs.getString("current_user_uuid", null)
            val ui = uiState
            if (userId != null && ui is QuestionsUiState.Loaded) {
                // Mapear respostas para questionId corretos vindos da API
                val apiMap = ui.questions.filter { it.category.equals("Carga de trabalho", true) }
                    .map { it.text to it }
                    .toMap()
                val payload = questions.mapIndexedNotNull { index, q ->
                    val value = answers.getOrNull(index) ?: return@mapIndexedNotNull null
                    val dto = apiMap[q.question] ?: return@mapIndexedNotNull null
                    AssessmentAnswerDto(questionId = dto.id, category = dto.category, value = value)
                }
                questionsViewModel.submitAssessment(userId, payload) { /* ignore callback for now */ }
            }
            onFinished(answers)
        }

        // Estados de carregamento/vazio
        if (uiState is QuestionsUiState.Loading || userAssessmentsState is UserAssessmentsUiState.Loading) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                CircularProgressIndicator()
                Text(text = "Carregando...")
            }
            return@Column
        }


        if (uiState is QuestionsUiState.Loaded && questions.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(text = "Nenhuma pergunta disponível.")
            }
            return@Column
        }

        // Tela de questionário reutilizável apenas se houver perguntas
        if (questions.isNotEmpty()) {
            QuestionnaireScreen(
                questions = questions,
                theme = workloadTheme,
                onBackPressed = onBackPressed,
                onFinished = handleFinished
            )
        }
    }
}
