package com.grupo.appsoftek.ui.theme.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.grupo.appsoftek.data.repository.AuthRepository
import java.util.UUID
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    
    private val authRepository = AuthRepository()
    private val sharedPreferences = application.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    
    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()
    
    private val _currentUuid = MutableStateFlow<String?>(null)
    val currentUuid: StateFlow<String?> = _currentUuid.asStateFlow()
    
    init {
        checkAuthStatus()
    }
    
    private fun checkAuthStatus() {
        viewModelScope.launch {
            try {
                val savedUuid = sharedPreferences.getString("current_user_uuid", null)
                
                if (savedUuid != null) {
                    _currentUuid.value = savedUuid
                    _authState.value = AuthState.Authenticated
                    return@launch
                }
                
                _authState.value = AuthState.NeedsFirstTimeSetup
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Erro desconhecido")
            }
        }
    }
    
    fun createFirstUser(password: String) {
        viewModelScope.launch {
            try {
                _authState.value = AuthState.Loading
                val uuid = UUID.randomUUID().toString()
                authRepository.register(uuid = uuid, password = password)
                    .getOrThrow()
                saveCurrentUuid(uuid)
                _currentUuid.value = uuid
                _authState.value = AuthState.Authenticated
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Erro ao criar usuário")
            }
        }
    }
    
    fun login(password: String) {
        viewModelScope.launch {
            try {
                _authState.value = AuthState.Loading
                // No backend atual, não há endpoint de login. Como a regra é apenas
                // não pedir novamente após primeiro acesso, consideramos login
                // desnecessário e permanecemos em NeedsFirstTimeSetup quando não há UUID salvo.
                _authState.value = AuthState.NeedsFirstTimeSetup
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Erro ao fazer login")
            }
        }
    }
    
    fun logout() {
        clearSavedUser()
        _currentUuid.value = null
        _authState.value = AuthState.NeedsFirstTimeSetup
    }
    
    private fun saveCurrentUuid(uuid: String) {
        sharedPreferences.edit()
            .putString("current_user_uuid", uuid)
            .apply()
    }
    
    private fun clearSavedUser() {
        sharedPreferences.edit()
            .remove("current_user_uuid")
            .apply()
    }
    
    fun clearError() {
        if (_authState.value is AuthState.Error) {
            viewModelScope.launch {
                _authState.value = AuthState.NeedsFirstTimeSetup
            }
        }
    }
}

sealed class AuthState {
    object Loading : AuthState()
    object NeedsFirstTimeSetup : AuthState()
    object NeedsLogin : AuthState()
    object Authenticated : AuthState()
    data class Error(val message: String) : AuthState()
}

