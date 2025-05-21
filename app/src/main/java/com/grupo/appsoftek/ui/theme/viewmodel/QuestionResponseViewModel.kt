package com.grupo.appsoftek.ui.theme.viewmodel

import android.app.Application
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.grupo.appsoftek.R
import com.grupo.appsoftek.data.database.AppDatabase
import com.grupo.appsoftek.data.model.QuestionResponse
import com.grupo.appsoftek.data.repository.QuestionResponseRepository
import com.grupo.appsoftek.ui.theme.components.MoodDay
import com.grupo.appsoftek.ui.theme.view.Section
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class QuestionResponseViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: QuestionResponseRepository

    // Mapa dos totais de perguntas por tipo (ajuste aos seus números reais)
    private val totalQuestionsMap = mapOf(
        "mood_tracking" to 1,
        "carga-de-trabalho" to 3,
        "produtividade" to 2,
        "clima" to 7,
        "comunicacao" to 4,
        "liderança" to 5
    )

    // Mapeamentos fixos de ícone e cores por tipo
    private val iconMap = mapOf(
        "mood_tracking" to R.drawable.hand_heart,
        "carga-de-trabalho" to R.drawable.briefcase,
        "produtividade" to R.drawable.network,
        "clima" to R.drawable.cloud,
        "comunicacao" to R.drawable.audio_lines,
        "liderança" to R.drawable.users
    )

    private val cardColorMap = mapOf(
        "mood_tracking" to Color(0xFF8DC63F),
        "carga-de-trabalho" to Color(0xFF8DC63F),
        "produtividade" to Color(0xFF8DC63F),
        "clima" to Color(0xFF8DC63F),
        "comunicacao" to Color(0xFF8DC63F),
        "liderança" to Color(0xFF8DC63F)
    )

    private val progressColorMap = mapOf(
        "mood_tracking" to Color(0xFF81C784),
        "carga-de-trabalho" to Color(0xFF64B5F6),
        "produtividade" to Color(0xFF9575CD),
        "clima" to Color(0xFFE57373),
        "comunicacao" to Color(0xFF7986CB),
        "liderança" to Color(0xFFFFB74D)
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
                            "mood_tracking" -> "Bem-estar emocional"
                            "carga-de-trabalho" -> "Carga de trabalho"
                            "produtividade" -> "Produtividade"
                            "clima" -> "Clima"
                            "comunicacao" -> "Comunicação"
                            "liderança" -> "Liderança"
                            else -> type
                        },
                        icon = iconMap[type] ?: R.drawable.splash_icon,
                        answered = counts[type] ?: 0,
                        total = total,
                        cardColor = cardColorMap[type] ?: Color.Gray,
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

    // Formato de data para mostrar “dd/MM”
    private val dateFormat =
        SimpleDateFormat("dd/MM", Locale.getDefault())

    // Mapeia o texto da resposta pro ícone correspondente
    private fun iconForAnswer(answer: String): Int =
        when (answer.lowercase()) {
            "feliz" -> R.drawable.icon_happy
            "ansioso" -> R.drawable.icon_anxious
            "cansado" -> R.drawable.icon_exausto
            "irritado" -> R.drawable.icon_angry
            "medo" -> R.drawable.icon_fear
            // … outros mapeamentos …
            else -> R.drawable.icon_happy // VALIDAR DEPOIS
        }

    // Fluxo de MoodDay (últimos 7 dias)
    val moodDaysFlow: StateFlow<List<MoodDay>> =
        repository.getLastMoodResponses(limit = 7)
            .map { responses ->
                responses.map { resp ->
                    MoodDay(
                        date = dateFormat.format(Date(resp.answeredAt)),
                        emojiResourceId = iconForAnswer(resp.answer)
                    )
                }
                    // já vem em ordem decrescente, mas você pode inverter:
                    .asReversed()
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    // Módulos de média diária
    private val liderancaAvg = repository.getAverageScoreToday("liderança")
    private val cargaAvg = repository.getAverageScoreToday("carga-de-trabalho")
    private val produtividadeAvg = repository.getAverageScoreToday("produtividade")
    private val climaAvg = repository.getAverageScoreToday("clima")

    // Funções auxiliares para texto e cor
    private fun labelForScore(s: Double) = when {
        s >= 4.5 -> "Ótimo"
        s >= 3.5 -> "Bom"
        s >= 2.5 -> "Regular"
        s >= 1.5 -> "Atenção"
        else -> "Crítico"
    }

    private fun colorForScore(s: Double) = when {
        s >= 4.5 -> Color(0xFF8DC63F)
        s >= 3.5 -> Color(0xFF8DC63F)
        s >= 2.5 -> Color(0xFFFFA000)
        s >= 1.5 -> Color(0xFFF44336)
        else -> Color(0xFFD32F2F)
    }

    data class AvgSection(
        val title: String,
        val average: Double,
        val label: String,
        val color: Color
    )

    // Combina as 4 médias num único fluxo de lista
    val avgSectionsFlow: StateFlow<List<AvgSection>> = combine(
        liderancaAvg,
        cargaAvg,
        produtividadeAvg,
        climaAvg
    ) { lid, car, prod, cli ->
        listOf(
            AvgSection(
                title = "Liderança",
                average = lid,
                label = labelForScore(lid),
                color = colorForScore(lid)
            ),
            AvgSection(
                title = "Carga de Trabalho",
                average = car,
                label = labelForScore(car),
                color = colorForScore(car)
            ),
            AvgSection(
                title = "Produtividade",
                average = prod,
                label = labelForScore(prod),
                color = colorForScore(prod)
            ),
            AvgSection(
                title = "Clima",
                average = cli,
                label = labelForScore(cli),
                color = colorForScore(cli)
            )
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )
}