package com.grupo.appsoftek.data.repository

import com.grupo.appsoftek.data.dao.QuestionResponseDao
import com.grupo.appsoftek.data.model.QuestionResponse
import kotlinx.coroutines.flow.Flow

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

    fun getResponsesByQuestionnaireType(questionnaireType: String): Flow<List<QuestionResponse>> {
        return questionResponseDao.getResponsesByQuestionnaireType(questionnaireType)
    }

    fun getAllResponses(): Flow<List<QuestionResponse>> {
        return questionResponseDao.getAllResponses()
    }
}