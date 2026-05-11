package com.lucy.myfoodaccessapp2.Data.Models

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val name: String,
    val email: String
)
