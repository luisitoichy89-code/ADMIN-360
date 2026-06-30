package com.admin360.core.supabase

import io.github.jan.supabase.SupabaseClient

object SupabaseProvider {
    lateinit var client: SupabaseClient

    fun init(client: SupabaseClient) {
        this.client = client
    }
}
