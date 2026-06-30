package com.admin360.feature.license.data

import com.admin360.core.supabase.SupabaseModule
import io.github.jan.supabase.postgrest.from
import com.admin360.core.model.LicenseDto

class LicenseRepository {

    suspend fun getLicense(clienteId: String): LicenseDto? {
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
