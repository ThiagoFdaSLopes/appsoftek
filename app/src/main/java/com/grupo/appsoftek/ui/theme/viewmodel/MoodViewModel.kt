package com.grupo.appsoftek.ui.theme.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grupo.appsoftek.data.model.MoodCheckIn
import com.grupo.appsoftek.domain.service.MoodService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MoodViewModel(private val service: MoodService) : ViewModel() {
    private val _todayMood = MutableStateFlow<MoodCheckIn?>(null)
    val todayMood: StateFlow<MoodCheckIn?> = _todayMood

    @RequiresApi(Build.VERSION_CODES.O)
    fun load() = viewModelScope.launch {
        _todayMood.value = service.getTodayMood()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun save(mood: Int, note: String?) = viewModelScope.launch {
        service.recordMood(mood, note)
        load()
    }
}
