package com.grupo.appsoftek.domain.service

import android.os.Build
import androidx.annotation.RequiresApi
import com.grupo.appsoftek.data.model.MoodCheckIn
import com.grupo.appsoftek.data.repository.MoodRepository
import java.time.LocalDate

class MoodService(private val repo: MoodRepository) {
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getTodayMood(): MoodCheckIn? =
        repo.fetchByDate(LocalDate.now())

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun recordMood(mood: Int, note: String?) {
        val entry = MoodCheckIn(
            date = LocalDate.now(),
            mood = mood,
            note = note
        )
        repo.save(entry)
    }
}
