package com.lucy.myfoodaccessapp2.Data.Models

import kotlinx.serialization.Serializable

@Serializable
data class FoodPost(
    val id: String? = null,
    val title: String,
    val location: String,
    val description: String,
    val created_at: String? = null,
    val user_id: String? = null
)
