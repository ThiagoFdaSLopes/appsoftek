package com.grupo.appsoftek.data.dao

import androidx.room.RoomDatabase

abstract class AppDatabase : RoomDatabase() {
    abstract fun moodDao(): MoodDao
}