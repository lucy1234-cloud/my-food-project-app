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

    fun postFood(title: String, location: String, description: String) {
        viewModelScope.launch {
            _isUploading.value = true
            val success = repository.uploadFoodPost(title, location, description)
            _uploadSuccess.value = success
            _isUploading.value = false
        }
    }
    
    fun resetUploadState() {
        _uploadSuccess.value = null
    }
}
