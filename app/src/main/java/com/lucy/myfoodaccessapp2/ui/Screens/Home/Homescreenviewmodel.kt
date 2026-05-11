package com.lucy.myfoodaccessapp2.ui.Screens.Home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucy.myfoodaccessapp2.Data.Models.FoodPost
import com.lucy.myfoodaccessapp2.Data.Repository.FoodRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: FoodRepository = FoodRepository()) : ViewModel() {

    private val _foodPosts = MutableStateFlow<List<FoodPost>>(emptyList())
    val foodPosts: StateFlow<List<FoodPost>> = _foodPosts

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        fetchPosts()
    }

    fun fetchPosts() {
        viewModelScope.launch {
            _isLoading.value = true
            val posts = repository.fetchFoodPosts()
            Log.d("HomeVM", "Fetched ${posts.size} posts from repository")
            _foodPosts.value = posts
            _isLoading.value = false
        }
    }
}