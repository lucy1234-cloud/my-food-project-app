package com.lucy.myfoodaccessapp2.Data.Repository

import android.util.Log
import com.lucy.myfoodaccessapp2.Data.Models.FoodPost
import com.lucy.myfoodaccessapp2.Data.Remote.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

class FoodRepository {

    companion object {
        private const val TAG = "FoodRepo"
    }

    suspend fun fetchFoodPosts(): List<FoodPost> {
        return withContext(Dispatchers.IO) {
            try {
                Log.d(TAG, "Fetching posts...")
                val result = SupabaseClient.client.postgrest["food_posts"]
                    .select()
                    .decodeList<FoodPost>()
                Log.d(TAG, "Fetched ${result.size} posts")
                result
            } catch (e: Exception) {
                Log.e(TAG, "Fetch failed: ${e.message}", e)
                emptyList()
            }
        }
    }

    suspend fun uploadFoodPost(title: String, location: String, description: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val user = SupabaseClient.client.auth.currentUserOrNull()
                Log.d(TAG, "Current user: ${user?.id ?: "NULL"}")

                if (user == null) {
                    return@withContext Result.failure(
                        Exception("Not logged in. Please sign in first.")
                    )
                }

                // FIXED: Don't send created_at — let Supabase auto-fill it with now()
                // Use JsonObject to only send the fields we need
                val postData = JsonObject(mapOf(
                    "title" to JsonPrimitive(title.trim()),
                    "location" to JsonPrimitive(location.trim()),
                    "description" to JsonPrimitive(description.trim().ifBlank { null }),
                    "user_id" to JsonPrimitive(user.id)
                ))

                SupabaseClient.client.postgrest["food_posts"].insert(postData)
                Log.d(TAG, "Insert successful!")
                Result.success(Unit)
            } catch (e: Exception) {
                Log.e(TAG, "Insert failed: ${e.message}", e)
                Result.failure(Exception(e.message ?: "Unknown error"))
            }
        }
    }
}