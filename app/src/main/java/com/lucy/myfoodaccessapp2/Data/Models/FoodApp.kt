package com.lucy.myfoodaccessapp2.Data.Models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FoodPost(
    val id: String? = null,           // ← FIXED: was Int?, now String? (UUID)
    val title: String,
    val location: String,
    val description: String? = null,  // ← FIXED: was String, now String? (nullable in DB)
    @SerialName("created_at")
    val createdAt: String,            // ← FIXED: was String?, now String (NOT NULL in DB)
    @SerialName("user_id")
    val userId: String                // ← FIXED: was String?, now String (NOT NULL in DB)
)