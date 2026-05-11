package com.lucy.myfoodaccessapp2.Data.Repository

import android.util.Log
import com.lucy.myfoodaccessapp2.Data.Models.FoodPost
import com.lucy.myfoodaccessapp2.Data.Remote.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FoodRepository {

    suspend fun fetchFoodPosts(): List<FoodPost> {
        return withContext(Dispatchers.IO) {
            try {
                SupabaseClient.client.postgrest["food_posts"]
                    .select()
                    .decodeList<FoodPost>()
            } catch (e: Exception) {
                Log.e("FoodRepo", "Fetch failed: ${e.message}")
                emptyList()
            }
        }
    }

    suspend fun uploadFoodPost(title: String, location: String, description: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val user = SupabaseClient.client.auth.currentUserOrNull()

                if (user == null) {
                    return@withContext Result.failure(
                        Exception("Not logged in. Please sign in first.")
                    )
                }

                val post = FoodPost(
                    title = title.trim(),
                    location = location.trim(),
                    description = description.trim(),
                    userId = user.id
                )

                SupabaseClient.client.postgrest["food_posts"].insert(post)
                Result.success(Unit)

            } catch (e: Exception) {
                Log.e("FoodRepo", "Insert failed: ${e.message}")
                Result.failure(Exception(e.message ?: "Unknown error"))
            }
        }
    }
}