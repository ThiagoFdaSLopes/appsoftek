package com.grupo.appsoftek.ui.theme.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.grupo.appsoftek.data.database.AppDatabase
import com.grupo.appsoftek.data.repository.QuestionResponseRepository
import kotlinx.coroutines.launch
import java.util.Date

data class MoodOption(
    val id: Int,
    val emoji: String,
    val label: String
)

// Mudar de ViewModel para AndroidViewModel para ter acesso ao contexto de aplicativo
class MoodTrackingViewModel(application: Application) : AndroidViewModel(application) {

    // Inicializar o repository para salvar as respostas
    private val repository: QuestionResponseRepository

    init {
        val questionResponseDao = AppDatabase.getDatabase(application).questionResponseDao()
        repository = QuestionResponseRepository(questionResponseDao)
    }

    // Lista de opções de humor
    val todayEmojis = listOf(
        MoodOption(1, "😲", "Triste"),
        MoodOption(2, "👋", "Alegre"),
        MoodOption(3, "😌", "Cansado"),
        MoodOption(4, "🥺", "Ansioso"),
        MoodOption(5, "😡", "Raiva"),
        MoodOption(6, "😃", "Feliz")
    )

    val feelingOptions = listOf(
        MoodOption(1, "😲", "Motivado"),
        MoodOption(2, "👋", "Cansado"),
        MoodOption(3, "😌", "Preocupado"),
        MoodOption(4, "🥺", "Estressado"),
        MoodOption(5, "😡", "Animado"),
        MoodOption(6, "😃", "Satisfeito")
    )

    // Estados para armazenar a seleção do usuário
    var selectedEmojiId by mutableStateOf<Int?>(null)
        private set

    var selectedFeelingId by mutableStateOf<Int?>(null)
        private set

    // Estado para verificar se o formulário está completo
    val isFormComplete: Boolean
        get() = selectedEmojiId != null && selectedFeelingId != null

    // Função para selecionar um emoji
    fun selectEmoji(id: Int) {
        selectedEmojiId = id
    }

    // Função para selecionar um sentimento
    fun selectFeeling(id: Int) {
        selectedFeelingId = id
    }

    // Função para obter o label do emoji selecionado
    private fun getSelectedEmojiLabel(): String? {
        return selectedEmojiId?.let { id ->
            todayEmojis.find { it.id == id }?.label
        }
    }

    // Função para obter o label do sentimento selecionado
    private fun getSelectedFeelingLabel(): String? {
        return selectedFeelingId?.let { id ->
            feelingOptions.find { it.id == id }?.label
        }
    }

    // Função para salvar as respostas
    fun saveResponses() {
        if (!isFormComplete) return

        viewModelScope.launch {
            val emojiLabel = getSelectedEmojiLabel() ?: return@launch
            val feelingLabel = getSelectedFeelingLabel() ?: return@launch

            // Criar lista de pares de pergunta e resposta
            val questionsWithAnswers = listOf(
                "Escolha o seu emoji de hoje!" to emojiLabel,
                "Como você se sente hoje?" to feelingLabel
            )

            // Salvar no banco de dados usando o repository
            repository.saveResponses("mood_tracking", questionsWithAnswers)

            // Reset dos valores após salvar (opcional)
            // selectedEmojiId = null
            // selectedFeelingId = null
        }
    }

    // Função para salvar as respostas numéricas (alternativa)
    fun saveNumericResponses() {
        if (!isFormComplete) return

        viewModelScope.launch {
            // Salvar as IDs diretamente como valores numéricos
            val questionsWithAnswers = listOf(
                "Escolha o seu emoji de hoje!" to selectedEmojiId.toString(),
                "Como você se sente hoje?" to selectedFeelingId.toString()
            )

            // Salvar no banco de dados usando o repository
            repository.saveResponses("mood_tracking", questionsWithAnswers)

            // Reset dos valores após salvar (opcional)
            // selectedEmojiId = null
            // selectedFeelingId = null
        }
    }
}