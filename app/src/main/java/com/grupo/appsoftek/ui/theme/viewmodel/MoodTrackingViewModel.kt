package com.grupo.appsoftek.ui.theme.viewmodel

import android.app.Application
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.grupo.appsoftek.data.database.AppDatabase
import com.grupo.appsoftek.data.repository.QuestionResponseRepository
import com.grupo.appsoftek.data.repository.DailyCheckinRepository
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
    private val dailyCheckinRepository: DailyCheckinRepository

    init {
        val questionResponseDao = AppDatabase.getDatabase(application).questionResponseDao()
        repository = QuestionResponseRepository(questionResponseDao)
        dailyCheckinRepository = DailyCheckinRepository(application)
    }

    // Lista de opções de humor
    val todayEmojis = listOf(
        MoodOption(1, "😢", "Triste"),
        MoodOption(2, "🙂", "Alegre"),
        MoodOption(3, "😥", "Cansado"),
        MoodOption(4, "😰", "Ansioso"),
        MoodOption(5, "😡", "Raiva"),
        MoodOption(6, "😃", "Feliz")
    )

    val feelingOptions = listOf(
        MoodOption(1, "😎", "Motivado"),
        MoodOption(2, "😥", "Cansado"),
        MoodOption(3, "😨", "Preocupado"),
        MoodOption(4, "😤", "Estressado"),
        MoodOption(5, "😃", "Animado"),
        MoodOption(6, "😉", "Satisfeito")
    )

    // Estados para armazenar a seleção do usuário
    var selectedEmojiId by mutableStateOf<Int?>(null)
        private set

    var selectedFeelingId by mutableStateOf<Int?>(null)
        private set

    // Estado para verificar se o formulário está completo
    val isFormComplete: Boolean
        get() = selectedEmojiId != null

    // Função para selecionar um emoji
    fun selectEmoji(id: Int) {
        selectedEmojiId = id
    }

    // Função para obter o label do emoji selecionado
    private fun getSelectedEmojiLabel(): String? {
        return selectedEmojiId?.let { id ->
            todayEmojis.find { it.id == id }?.label
        }
    }

    // Função para salvar as respostas
    fun saveResponses(onSuccess: () -> Unit = {}, onError: (String) -> Unit = {}) {
        if (!isFormComplete) return

        viewModelScope.launch {
            val emojiLabel = getSelectedEmojiLabel() ?: return@launch
            
            // Obter userId do SharedPreferences
            val sharedPreferences = getApplication<Application>().getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
            var userId = sharedPreferences.getString("user_id", null)
            
            println("DEBUG: Tentando recuperar user_id do SharedPreferences")
            println("DEBUG: user_id encontrado: $userId")
            
            // Fallback: tentar usar current_user_uuid se user_id não estiver disponível
            if (userId == null) {
                userId = sharedPreferences.getString("current_user_uuid", null)
                println("DEBUG: user_id é null, tentando current_user_uuid: $userId")
            }
            
            if (userId == null) {
                println("DEBUG: user_id é null, verificando todas as chaves do SharedPreferences")
                val allKeys = sharedPreferences.all.keys
                println("DEBUG: Chaves disponíveis: $allKeys")
                onError("User ID não encontrado")
                return@launch
            }

            // Mapear o emoji para o formato esperado pela API
            val moodValue = when (emojiLabel) {
                "Triste" -> "sad_emoji"
                "Alegre" -> "happy_emoji"
                "Cansado" -> "tired_emoji"
                "Ansioso" -> "anxious_emoji"
                "Raiva" -> "angry_emoji"
                "Feliz" -> "happy_emoji"
                else -> "neutral_emoji"
            }

            // Enviar para a API
            dailyCheckinRepository.submitDailyCheckin(userId, moodValue)
                .onSuccess {
                    // Também salvar localmente como backup
                    val questionsWithAnswers = listOf(
                        "Escolha o seu emoji de hoje!" to emojiLabel
                    )
                    repository.saveResponses("mood_tracking", questionsWithAnswers)
                    
                    onSuccess()
                }
                .onFailure { throwable ->
                    onError(throwable.message ?: "Erro ao enviar mood")
                }
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