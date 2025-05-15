package com.grupo.appsoftek.factory

import android.content.Context
import androidx.room.Room
import com.grupo.appsoftek.data.dao.AppDatabase
import com.grupo.appsoftek.data.repository.MoodRepository
import com.grupo.appsoftek.domain.service.MoodService
import com.grupo.appsoftek.ui.theme.viewmodel.MoodViewModel

object Factory {
    fun provideDatabase(context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "app.db").build()

    fun provideMoodViewModel(context: Context): MoodViewModel {
        val db = provideDatabase(context)
        val repo = MoodRepository(db.moodDao())
        val service = MoodService(repo)
        return MoodViewModel(service)
    }

}
