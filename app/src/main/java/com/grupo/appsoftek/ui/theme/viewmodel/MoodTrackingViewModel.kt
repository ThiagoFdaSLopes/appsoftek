package com.grupo.appsoftek.ui.theme.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

data class MoodOption(
    val id: Int,
    val emoji: String,
    val label: String
)

class MoodTrackingViewModel : ViewModel() {

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

    // Função para salvar as respostas
    fun saveResponses() {
        viewModelScope.launch {
            // Aqui você implementaria a lógica para salvar as respostas
            // Por exemplo, salvar no banco de dados local ou enviar para API
            // Exemplo: repository.saveMoodEntry(selectedEmojiId, selectedFeelingId, Date())

            // Reset dos valores após salvar
            // selectedEmojiId = null
            // selectedFeelingId = null
        }
    }
}