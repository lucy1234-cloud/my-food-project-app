package com.lucy.myfoodaccessapp2.Data.Models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FoodPost(
    val id: Int? = null,
    val title: String,
    val location: String,
    val description: String,
    @SerialName("created_at")
    val createdAt: String? = null,
    @SerialName("user_id")
    val userId: String? = null
)