package com.grupo.appsoftek.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "question_responses")
data class QuestionResponse(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val questionnaireType: String, // Tipo de questionário (ex: "comunicacao", "clima", etc.)
    val questionText: String,      // Texto da pergunta
    val answer: String,            // Resposta selecionada
    val answeredAt: Long           // Data e hora da resposta
)