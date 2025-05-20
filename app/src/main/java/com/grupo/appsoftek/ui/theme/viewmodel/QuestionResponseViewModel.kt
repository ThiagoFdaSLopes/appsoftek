package com.grupo.appsoftek.ui.theme.viewmodel

import android.app.Application
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.grupo.appsoftek.R
import com.grupo.appsoftek.data.database.AppDatabase
import com.grupo.appsoftek.data.model.QuestionResponse
import com.grupo.appsoftek.data.repository.QuestionResponseRepository
import com.grupo.appsoftek.ui.theme.view.Section
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class QuestionResponseViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: QuestionResponseRepository

    // Mapa dos totais de perguntas por tipo (ajuste aos seus números reais)
    private val totalQuestionsMap = mapOf(
        "mood_tracking"      to 1,
        "carga-de-trabalho"         to 3,
        "produtividade" to 2,
        "clima"         to 7,
        "comunicacao"   to 4,
        "liderança"     to 5
    )

    // Mapeamentos fixos de ícone e cores por tipo
    private val iconMap = mapOf(
        "mood_tracking"      to R.drawable.hand_heart,
        "carga-de-trabalho"         to R.drawable.briefcase,
        "produtividade" to R.drawable.network,
        "clima"         to R.drawable.cloud,
        "comunicacao"   to R.drawable.audio_lines,
        "liderança"     to R.drawable.users
    )

    private val cardColorMap = mapOf(
        "mood_tracking"      to Color(0xFF8DC63F),
        "carga-de-trabalho"         to Color(0xFF8DC63F),
        "produtividade" to Color(0xFF8DC63F),
        "clima"         to Color(0xFF8DC63F),
        "comunicacao"   to Color(0xFF8DC63F),
        "liderança"     to Color(0xFF8DC63F)
    )

    private val progressColorMap = mapOf(
        "mood_tracking"      to Color(0xFF81C784),
        "carga-de-trabalho"         to Color(0xFF64B5F6),
        "produtividade" to Color(0xFF9575CD),
        "clima"         to Color(0xFFE57373),
        "comunicacao"   to Color(0xFF7986CB),
        "liderança"     to Color(0xFFFFB74D)
    )

    init {
        val questionResponseDao = AppDatabase.getDatabase(application).questionResponseDao()
        repository = QuestionResponseRepository(questionResponseDao)
    }

    // Emissão de lista de Section
    val sectionsFlow: StateFlow<List<Section>> =
        repository.getTodayCountsMap()
            .map { counts ->
                totalQuestionsMap.map { (type, total) ->
                    Section(
                        title = when (type) {
                            "mood_tracking"      -> "Bem-estar emocional"
                            "carga-de-trabalho"         -> "Carga de trabalho"
                            "produtividade" -> "Produtividade"
                            "clima"         -> "Clima"
                            "comunicacao"   -> "Comunicação"
                            "liderança"     -> "Liderança"
                            else            -> type
                        },
                        icon          = iconMap[type] ?: R.drawable.splash_icon,
                        answered      = counts[type] ?: 0,
                        total         = total,
                        cardColor     = cardColorMap[type] ?: Color.Gray,
                        progressColor = progressColorMap[type] ?: Color.Blue
                    )
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    fun saveQuestionnaireResponses(
        questionnaireType: String,
        questionsWithAnswers: List<Pair<String, String?>>
    ) {
        viewModelScope.launch {
            repository.saveResponses(questionnaireType, questionsWithAnswers)
        }
    }

    val totalResponsesCount: StateFlow<Int> =
        repository.getAllResponsesCount()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = 0
            )

    fun getResponsesByQuestionnaireType(questionnaireType: String): Flow<List<QuestionResponse>> {
        return repository.getResponsesByQuestionnaireType(questionnaireType)
    }

    fun getAllResponses(): Flow<List<QuestionResponse>> {
        return repository.getAllResponses()
    }

    suspend fun hasAnsweredToday(questionnaireType: String): Boolean {
        return repository.hasAnsweredToday(questionnaireType)
    }
}