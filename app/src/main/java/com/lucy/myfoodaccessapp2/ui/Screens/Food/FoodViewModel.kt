package com.lucy.myfoodaccessapp2.ui.Screens.Food

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucy.myfoodaccessapp2.Data.Repository.FoodRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FoodViewModel(private val repository: FoodRepository = FoodRepository()) : ViewModel() {

    private val _isUploading = MutableStateFlow(false)
    val isUploading: StateFlow<Boolean> = _isUploading

    private val _uploadSuccess = MutableStateFlow<Boolean?>(null)
    val uploadSuccess: StateFlow<Boolean?> = _uploadSuccess

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun postFood(title: String, location: String, description: String) {
        viewModelScope.launch {
            _isUploading.value = true
            _errorMessage.value = null

            val result = repository.uploadFoodPost(title, location, description)

            result.onSuccess {
                _uploadSuccess.value = true
            }.onFailure { error ->
                _uploadSuccess.value = false
                _errorMessage.value = error.message ?: "Failed to post"
            }

            _isUploading.value = false
        }
    }

    fun resetUploadState() {
        _uploadSuccess.value = null
        _errorMessage.value = null
    }
}