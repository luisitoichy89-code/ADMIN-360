package com.admin360.core.security

import com.admin360.core.model.LicenseDto
import com.admin360.core.supabase.SupabaseModule
import io.github.jan.supabase.postgrest.from

class LicenseGuard {

    suspend fun validateOrLogout(
        negocioId: String,
        onInvalid: () -> Unit
    ) {
        val lic = SupabaseModule.client
            .from("licencias")
            .select {
                filter { eq("negocio_id", negocioId) }
            }
            .decodeList<LicenseDto>()
            .firstOrNull()

        if (lic == null || lic.estado != "ACTIVA") {
            onInvalid()
        }
    }
}
