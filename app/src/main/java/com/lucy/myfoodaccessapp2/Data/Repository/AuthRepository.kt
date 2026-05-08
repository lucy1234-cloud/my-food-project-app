package com.lucy.myfoodaccessapp2.Data.Repository

import com.lucy.myfoodaccessapp2.Data.Remote.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class AuthRepository {

    suspend fun signUp(email: String, password: String, name: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                SupabaseClient.client.auth.signUpWith(Email) {
                    this.email = email
                    this.password = password
                    // Hii inatuma jina kwenye metadata ili SQL trigger ilichukue
                    data = buildJsonObject {
                        put("full_name", name)
                    }
                }
                true
            } catch (e: Exception) {
                false
            }
        }
    }

    suspend fun signIn(email: String, password: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                SupabaseClient.client.auth.signInWith(Email) {
                    this.email = email
                    this.password = password
                }
                true
            } catch (e: Exception) {
                false
            }
        }
    }

    suspend fun signOut() {
        withContext(Dispatchers.IO) {
            SupabaseClient.client.auth.signOut()
        }
    }

    fun getCurrentUser() = SupabaseClient.client.auth.currentUserOrNull()
}
