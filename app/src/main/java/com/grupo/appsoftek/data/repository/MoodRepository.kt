package com.grupo.appsoftek.data.repository

import com.grupo.appsoftek.data.dao.MoodDao
import com.grupo.appsoftek.data.model.MoodCheckIn
import java.time.LocalDate

class MoodRepository(private val dao: MoodDao) {
    suspend fun fetchByDate(date: LocalDate): MoodCheckIn? =
        dao.getByDate(date)

    suspend fun save(checkIn: MoodCheckIn) {
        dao.insert(checkIn)
    }
}
