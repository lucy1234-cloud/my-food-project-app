package com.lucy.myfoodaccessapp2.Data.Remote

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.postgrest.Postgrest

object SupabaseClient {
    // Replace these with your actual Supabase URL and Anon Key from your project settings
    private const val SUPABASE_URL = "https://hdzqeescqntlspzdogmq.supabase.co"
    private const val SUPABASE_KEY = "sb_publishable_S6FVCheXB0j6Pzxn2kDd-w_EPDh1fVe"

    val client: SupabaseClient = createSupabaseClient(
        supabaseUrl = SUPABASE_URL,
        supabaseKey = SUPABASE_KEY
    ) {
        install(Auth)
        install(Postgrest)
    }
}
