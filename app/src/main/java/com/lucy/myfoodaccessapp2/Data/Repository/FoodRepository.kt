package com.lucy.myfoodaccessapp2.Data.Repository

import com.lucy.myfoodaccessapp2.Data.Models.FoodPost
import com.lucy.myfoodaccessapp2.Data.Remote.SupabaseClient
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
                emptyList()
            }
        }
    }

    suspend fun uploadFoodPost(title: String, location: String, description: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val post = FoodPost(
                    title = title,
                    location = location,
                    description = description
                )
                SupabaseClient.client.postgrest["food_posts"].insert(post)
                true
            } catch (e: Exception) {
                false
            }
        }
    }
}
