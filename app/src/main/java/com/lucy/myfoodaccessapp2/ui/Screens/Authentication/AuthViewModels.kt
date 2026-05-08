package com.lucy.myfoodaccessapp2.ui.Screens.Authentication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucy.myfoodaccessapp2.Data.Repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: AuthRepository = AuthRepository()) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _authState = MutableStateFlow<AuthResult?>(null)
    val authState: StateFlow<AuthResult?> = _authState

    fun signUp(email: String, password: String, name: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val success = repository.signUp(email, password, name)
            _authState.value = if (success) AuthResult.Success else AuthResult.Error("Signup failed")
            _isLoading.value = false
        }
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val success = repository.signIn(email, password)
            _authState.value = if (success) AuthResult.Success else AuthResult.Error("Login failed")
            _isLoading.value = false
        }
    }

    fun resetState() {
        _authState.value = null
    }
}

sealed class AuthResult {
    object Success : AuthResult()
    data class Error(val message: String) : AuthResult()
}
