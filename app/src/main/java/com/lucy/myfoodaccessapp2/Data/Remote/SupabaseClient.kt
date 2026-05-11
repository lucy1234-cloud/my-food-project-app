package com.lucy.myfoodaccessapp2.Data.Remote

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.postgrest.Postgrest

object SupabaseClient {
    val client: SupabaseClient = createSupabaseClient(
        supabaseUrl = "https://hdzqeescqntlspzdogmq.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImhkenFlZXNjcW50bHNwemRvZ21xIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NzgxNTc2NTIsImV4cCI6MjA5MzczMzY1Mn0.IKCu0eizBQHARoziGSFv3_jkts2mRwv-i2HwdoV_n3A"
    ) {
        install(Auth)
        install(Postgrest)
    }
}