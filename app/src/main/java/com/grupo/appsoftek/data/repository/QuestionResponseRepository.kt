package com.grupo.appsoftek.data.repository

import com.grupo.appsoftek.data.dao.QuestionResponseDao
import com.grupo.appsoftek.data.model.QuestionResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class QuestionResponseRepository(private val questionResponseDao: QuestionResponseDao) {

    suspend fun saveResponse(
        questionnaireType: String,
        questionText: String,
        answer: String
    ): Long {
        val response = QuestionResponse(
            questionnaireType = questionnaireType,
            questionText = questionText,
            answer = answer,
            answeredAt = System.currentTimeMillis()
        )
        return questionResponseDao.insertResponse(response)
    }

    suspend fun saveResponses(
        questionnaireType: String,
        questionsWithAnswers: List<Pair<String, String?>>
    ): List<Long> {
        val responses = questionsWithAnswers
            .filter { it.second != null }
            .map { (question, answer) ->
                QuestionResponse(
                    questionnaireType = questionnaireType,
                    questionText = question,
                    answer = answer!!,
                    answeredAt = System.currentTimeMillis()
                )
            }
        return questionResponseDao.insertResponses(responses)
    }

    fun getAllResponsesCount(): Flow<Int> = questionResponseDao.countAllResponses()


    fun getResponsesByQuestionnaireType(questionnaireType: String): Flow<List<QuestionResponse>> {
        return questionResponseDao.getResponsesByQuestionnaireType(questionnaireType)
    }

    fun getAllResponses(): Flow<List<QuestionResponse>> {
        return questionResponseDao.getAllResponses()
    }

    suspend fun hasAnsweredToday(questionnaireType: String): Boolean {
        return questionResponseDao.countToday(questionnaireType) > 0
    }

    fun getTodayCountsMap(): Flow<Map<String, Int>> =
        questionResponseDao.getTodayCounts()
            .map { list -> list.associate { it.questionnaireType to it.count } }

    fun getLastMoodResponses(limit: Int = 7): Flow<List<QuestionResponse>> =
        questionResponseDao.getLastResponses(type = "mood_tracking", limit = limit)

    fun getAverageToday(type: String): Flow<Double> =
        questionResponseDao.getAverageToday(type)
            .map { it ?: 0.0 }

    // mapeia string → nota de 1 a 5, conforme cada questionário
    private fun answerToScore(
        questionnaireType: String,
        answer: String
    ): Int = when (questionnaireType) {

        "carga-de-trabalho" -> listOf("Muito Leve", "Leve", "Média", "Alta", "Muito Alta")
            .indexOf(answer).let { if (it >= 0) it + 1 else 0 }

        "produtividade" -> listOf("Nunca", "Raramente", "Às vezes", "Frequentemente", "Sempre")
            .indexOf(answer).let { if (it >= 0) it + 1 else 0 }

        "clima", "liderança" -> answer.toIntOrNull()?.coerceIn(1, 5) ?: 0

        else -> 0
    }

    /** Média de hoje, já como Double de 1.0–5.0 */
    fun getAverageScoreToday(type: String): Flow<Double> =
        questionResponseDao.getTodayResponses(type)
            .map { responses ->
                val scores = responses.map { answerToScore(type, it.answer) }
                if (scores.isEmpty()) 0.0 else scores.average()
            }
}