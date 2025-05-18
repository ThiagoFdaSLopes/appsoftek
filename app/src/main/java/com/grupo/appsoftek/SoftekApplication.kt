package com.grupo.appsoftek

import android.app.Application
import com.grupo.appsoftek.data.database.AppDatabase

class SoftekApplication : Application() {
    // O database será inicializado de forma lazy quando for acessado pela primeira vez
    val database by lazy { AppDatabase.getDatabase(this) }
}