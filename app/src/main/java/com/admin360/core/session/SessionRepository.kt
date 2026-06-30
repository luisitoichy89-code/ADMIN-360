package com.admin360.core.session

import com.admin360.core.supabase.SupabaseModule

class SessionRepository {

    fun getUserId(): String? {
        return SupabaseModule.client.auth.currentUserOrNull()?.id
    }

    fun isLogged(): Boolean {
        return SupabaseModule.client.auth.currentUserOrNull() != null
    }
}
