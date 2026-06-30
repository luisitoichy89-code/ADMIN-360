package com.admin360.core.supabase

import io.github.jan.supabase.SupabaseClient

object SupabaseInit {

    lateinit var client: SupabaseClient

    fun init(c: SupabaseClient) {
        client = c
    }
}
