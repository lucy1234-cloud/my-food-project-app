package com.lucy.myfoodaccessapp2.Data.Repository

import com.lucy.myfoodaccessapp2.Data.Remote.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class AuthServiceRepository {

    private val auth = SupabaseClient.client.auth

    suspend fun signUp(email: String, password: String, fullName: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                auth.signUpWith(Email) {
                    this.email = email
                    this.password = password
                    data = buildJsonObject {
                        put("full_name", fullName)
                    }
                }
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun signIn(email: String, password: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                auth.signInWith(Email) {
                    this.email = email
                    this.password = password
                }
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun signOut(): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                auth.signOut()
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    fun getCurrentUser() = auth.currentUserOrNull()

    suspend fun resetPassword(email: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                auth.resetPasswordForEmail(email)
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}
