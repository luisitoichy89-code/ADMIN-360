package com.admin360.core.repository

import com.admin360.core.model.LicenseDto
import com.admin360.core.session.SessionManager
import com.admin360.core.supabase.SupabaseModule
import io.github.jan.supabase.postgrest.from

class LicenseRepository {

    suspend fun getLicense(): LicenseDto? {
        val clienteId = SessionManager.clienteId
        return SupabaseModule.client
            .from("licencias")
            .select {
                filter {
                    eq("cliente_id", clienteId)
                }
            }
            .decodeSingleOrNull()
    }

    suspend fun getLicenseByClienteId(clienteId: String): LicenseDto? {
        return SupabaseModule.client
            .from("licencias")
            .select {
                filter {
                    eq("cliente_id", clienteId)
                }
            }
            .decodeSingleOrNull()
    }
}
