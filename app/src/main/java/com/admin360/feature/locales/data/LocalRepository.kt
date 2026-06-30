package com.admin360.feature.locales.data

import com.admin360.core.supabase.SupabaseModule
import io.github.jan.supabase.postgrest.from

data class LocalDto(
    val id: String,
    val nombre: String,
    val negocio_id: String,
    val cliente_id: String
)

class LocalRepository {

    suspend fun get(clienteId: String) =
        SupabaseModule.client.from("locales")
            .select { filter { eq("cliente_id", clienteId) } }
            .decodeList<LocalDto>()
}
