package com.grupo.appsoftek.factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.grupo.appsoftek.ui.theme.viewmodel.MoodTrackingViewModel

class MoodTrackingViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MoodTrackingViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MoodTrackingViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}