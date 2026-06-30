package com.admin360.feature.license.data

import com.admin360.core.supabase.SupabaseModule
import io.github.jan.supabase.postgrest.from

class LicenseAdminRepository {

    suspend fun renew(id: String) {
        SupabaseModule.client.from("licencias")
            .update({
                set("estado", "ACTIVA")
            }) {
                filter { eq("id", id) }
            }
    }
}
