package com.admin360.core.auth

import com.admin360.core.supabase.SupabaseModule
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email

class AuthRepository {

    suspend fun login(email: String, password: String) {
        SupabaseModule.client.auth.signInWith(
            Email {
                this.email = email
                this.password = password
            }
        )
    }

    suspend fun logout() {
        SupabaseModule.client.auth.signOut()
    }
}
