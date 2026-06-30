package com.admin360.feature.dashboard.data

import com.admin360.core.session.SessionManager
import com.admin360.core.supabase.SupabaseModule
import io.github.jan.supabase.postgrest.from

class DashboardRepository {

    suspend fun getNegociosCount(): Int {
        val clienteId = SessionManager.clienteId
        return SupabaseModule.client
            .from("negocios")
            .select {
                filter { eq("cliente_id", clienteId) }
            }
            .decodeList<Any>()
            .size
    }

    suspend fun getLocalesCount(): Int {
        val clienteId = SessionManager.clienteId
        return SupabaseModule.client
            .from("locales")
            .select {
                filter { eq("cliente_id", clienteId) }
            }
            .decodeList<Any>()
            .size
    }

    suspend fun getUsuariosCount(): Int {
        val clienteId = SessionManager.clienteId
        return SupabaseModule.client
            .from("usuarios")
            .select {
                filter { eq("cliente_id", clienteId) }
            }
            .decodeList<Any>()
            .size
    }

    suspend fun getLicenciasActivasCount(): Int {
        val clienteId = SessionManager.clienteId
        return SupabaseModule.client
            .from("licencias")
            .select {
                filter {
                    eq("cliente_id", clienteId)
                    eq("estado", "ACTIVA")
                }
            }
            .decodeList<Any>()
            .size
    }
}
