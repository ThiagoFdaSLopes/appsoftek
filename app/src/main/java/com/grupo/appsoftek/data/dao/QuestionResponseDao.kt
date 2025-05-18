package com.grupo.appsoftek.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.grupo.appsoftek.data.model.QuestionResponse
import kotlinx.coroutines.flow.Flow

@Dao
interface QuestionResponseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResponse(response: QuestionResponse): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResponses(responses: List<QuestionResponse>): List<Long>

    @Query("SELECT * FROM question_responses WHERE questionnaireType = :questionnaireType ORDER BY answeredAt DESC")
    fun getResponsesByQuestionnaireType(questionnaireType: String): Flow<List<QuestionResponse>>

    @Query("SELECT * FROM question_responses ORDER BY answeredAt DESC")
    fun getAllResponses(): Flow<List<QuestionResponse>>
}