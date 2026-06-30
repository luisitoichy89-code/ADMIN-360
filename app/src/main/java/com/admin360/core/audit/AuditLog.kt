package com.admin360.core.audit

import com.admin360.core.session.SessionManager
import com.admin360.core.supabase.SupabaseModule

suspend fun log(action: String) {

    try {
        SupabaseModule.client
            .from("logs")
            .insert(
                mapOf(
                    "usuario_id" to SessionManager.session.value.usuarioId,
                    "accion" to action
                )
            )
    } catch (e: Exception) {
        // Silent fail para no romper flujo
    }
}
