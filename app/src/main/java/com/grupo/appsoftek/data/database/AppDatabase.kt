package com.grupo.appsoftek.data.database

import androidx.room.RoomDatabase
import com.grupo.appsoftek.data.dao.QuestionResponseDao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import com.grupo.appsoftek.data.model.QuestionResponse

@Database(entities = [QuestionResponse::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun questionResponseDao(): QuestionResponseDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "softek_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}