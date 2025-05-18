package com.grupo.appsoftek.ui.theme.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.grupo.appsoftek.data.database.AppDatabase
import com.grupo.appsoftek.data.model.QuestionResponse
import com.grupo.appsoftek.data.repository.QuestionResponseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class QuestionResponseViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: QuestionResponseRepository

    init {
        val questionResponseDao = AppDatabase.getDatabase(application).questionResponseDao()
        repository = QuestionResponseRepository(questionResponseDao)
    }

    fun saveQuestionnaireResponses(
        questionnaireType: String,
        questionsWithAnswers: List<Pair<String, String?>>
    ) {
        viewModelScope.launch {
            repository.saveResponses(questionnaireType, questionsWithAnswers)
        }
    }

    fun getResponsesByQuestionnaireType(questionnaireType: String): Flow<List<QuestionResponse>> {
        return repository.getResponsesByQuestionnaireType(questionnaireType)
    }

    fun getAllResponses(): Flow<List<QuestionResponse>> {
        return repository.getAllResponses()
    }
}