package com.lucy.myfoodaccessapp2.ui.Screens.Authentication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucy.myfoodaccessapp2.Data.Repository.AuthServiceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: AuthServiceRepository = AuthServiceRepository()) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _authState = MutableStateFlow<AuthResult?>(null)
    val authState: StateFlow<AuthResult?> = _authState

    fun signUp(email: String, password: String, name: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.signUp(email, password, name)
            result.onSuccess {
                _authState.value = AuthResult.Success
            }.onFailure { e ->
                val errorMessage = when {
                    e.message?.contains("rate limit", ignoreCase = true) == true -> 
                        "Email rate limit exceeded. Please wait a few minutes or increase the limit in Supabase Dashboard (Authentication -> Settings)."
                    e.message?.contains("already registered", ignoreCase = true) == true ->
                        "This email is already registered. Please try logging in."
                    else -> e.localizedMessage ?: e.message ?: "Signup failed. Please check your internet connection."
                }
                _authState.value = AuthResult.Error(errorMessage)
            }
            _isLoading.value = false
        }
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.signIn(email, password)
            result.onSuccess {
                _authState.value = AuthResult.Success
            }.onFailure { e ->
                val errorMessage = when {
                    e.message?.contains("Invalid login credentials", ignoreCase = true) == true ->
                        "Invalid email or password."
                    e.message?.contains("Email not confirmed", ignoreCase = true) == true ->
                        "Please verify your email address before logging in."
                    else -> e.localizedMessage ?: e.message ?: "Login failed. Please check your credentials."
                }
                _authState.value = AuthResult.Error(errorMessage)
            }
            _isLoading.value = false
        }
    }

    fun resetState() {
        _authState.value = null
    }

    fun signOut() {
        viewModelScope.launch {
            repository.signOut()
        }
    }

    fun isUserLoggedIn(): Boolean {
        return repository.getCurrentUser() != null
    }
}

sealed class AuthResult {
    object Success : AuthResult()
    data class Error(val message: String) : AuthResult()
}
