package com.grupo.appsoftek.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grupo.appsoftek.data.model.Quote
import com.grupo.appsoftek.data.repository.QuoteRepository
import kotlinx.coroutines.launch

class QuoteViewModel : ViewModel() {
    private val repository = QuoteRepository()

    private val _quote = MutableLiveData<Quote?>()
    val quote: LiveData<Quote?> = _quote

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun fetchRandomQuote() {
        _isLoading.value = true
        viewModelScope.launch {
            repository.getRandomQuote()
                .onSuccess { quote ->
                    _quote.value = quote
                    _isLoading.value = false
                }
                .onFailure { exception ->
                    _error.value = exception.message ?: "Erro desconhecido"
                    _isLoading.value = false
                }
        }
    }
}
