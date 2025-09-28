package com.grupo.appsoftek.ui.theme.view

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.grupo.appsoftek.ui.theme.components.QuestionnaireScreen
import com.grupo.appsoftek.ui.theme.viewmodel.QuestionResponseViewModel
import com.grupo.appsoftek.ui.theme.viewmodel.QuestionsUiState
import com.grupo.appsoftek.ui.theme.viewmodel.QuestionsViewModel
import com.grupo.appsoftek.ui.theme.viewmodel.UserAssessmentsViewModel
import com.grupo.appsoftek.ui.theme.viewmodel.UserAssessmentsUiState
import com.grupo.appsoftek.data.network.AssessmentAnswerDto
import androidx.compose.ui.platform.LocalContext

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
            val hasAnswered = userAssessmentsViewModel.hasAnsweredCategory("Produtividade")
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

    val remoteQuestions: List<MentalHealthQuestion>? = when (uiState) {
        is QuestionsUiState.Loaded -> {
            val loaded = (uiState as QuestionsUiState.Loaded).questions
            val filtered = loaded.filter { it.category.equals("Produtividade", ignoreCase = true) }
            if (filtered.isNotEmpty()) filtered.map {
                MentalHealthQuestion(
                    question = it.text,
                    options = listOf("Nunca", "Raramente", "Às vezes", "Frequentemente", "Sempre")
                )
            } else null
        }
        else -> null
    }

    // Converter de MentalHealthQuestion para Question
    val questions = (remoteQuestions ?: emptyList()).map {
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

    val handleFinished = { answers: List<String?> ->
        val prefs = appContext.getSharedPreferences("auth_prefs", android.content.Context.MODE_PRIVATE)
        val userId = prefs.getString("current_user_uuid", null)
        val ui = uiState
        if (userId != null && ui is QuestionsUiState.Loaded) {
            val apiMap = ui.questions.filter { it.category.equals("Produtividade", true) }
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

    // Estados de carregamento/vazio
    when (uiState) {
        is QuestionsUiState.Loading -> {
            androidx.compose.material3.CircularProgressIndicator()
            androidx.compose.material3.Text(text = "Carregando...")
            return
        }
        is QuestionsUiState.Loaded -> {
            
            if (questions.isEmpty()) {
                androidx.compose.material3.Text(text = "Nenhuma pergunta disponível.")
                return
            }
        }
        is QuestionsUiState.Error -> {
            // opcional: mensagem de erro
        }
        else -> {}
    }

    // Usar o componente reutilizável apenas se houver perguntas
    if (questions.isNotEmpty()) {
        QuestionnaireScreen(
            questions = questions,
            theme = mentalHealthTheme,
            onBackPressed = onBackPressed,
            onFinished = handleFinished
        )
    }
}