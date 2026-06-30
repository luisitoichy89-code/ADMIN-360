package com.admin360.feature.dashboard.data

import com.admin360.core.supabase.SupabaseModule
import io.github.jan.supabase.postgrest.from

class DashboardRepository {

    suspend fun getNegociosCount(clienteId: String): Int {
        return SupabaseModule.client
            .from("negocios")
            .select {
                filter { eq("cliente_id", clienteId) }
            }
            .decodeList<Any>()
            .size
    }

    suspend fun getUsuariosCount(clienteId: String): Int {
        return SupabaseModule.client
            .from("usuarios")
            .select {
                filter { eq("cliente_id", clienteId) }
            }
            .decodeList<Any>()
            .size
    }

    suspend fun getLocalesCount(clienteId: String): Int {
        return SupabaseModule.client
            .from("locales")
            .select {
                filter { eq("cliente_id", clienteId) }
            }
            .decodeList<Any>()
            .size
    }

    suspend fun getLicenciasActivasCount(clienteId: String): Int {
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
