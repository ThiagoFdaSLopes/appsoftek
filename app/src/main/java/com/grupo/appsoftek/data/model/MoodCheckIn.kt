package com.grupo.appsoftek.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "mood_checkins")
data class MoodCheckIn(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: LocalDate,
    val mood: Int,
    val note: String?
)
