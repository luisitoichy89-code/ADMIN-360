package com.admin360.core.supabase

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.auth.Auth

object SupabaseModule {

    val client: SupabaseClient = createSupabaseClient(
        supabaseUrl = "https://YOUR_PROJECT.supabase.co",
        supabaseKey = "YOUR_ANON_KEY"
    ) {
        install(Auth)
        install(Postgrest)
    }
}
