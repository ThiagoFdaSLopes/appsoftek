package com.grupo.appsoftek.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.grupo.appsoftek.data.model.MoodCheckIn
import java.time.LocalDate

@Dao
interface MoodDao {
    @Query("SELECT * FROM mood_checkins WHERE date = :date")
    suspend fun getByDate(date: LocalDate): MoodCheckIn?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(checkIn: MoodCheckIn)
}
