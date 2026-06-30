package com.admin360.feature.negocios.data

import com.admin360.core.supabase.SupabaseModule
import io.github.jan.supabase.postgrest.from

data class NegocioDto(
    val id: String,
    val nombre: String,
    val cliente_id: String
)

class NegocioRepository {

    suspend fun get(clienteId: String) =
        SupabaseModule.client.from("negocios")
            .select { filter { eq("cliente_id", clienteId) } }
            .decodeList<NegocioDto>()

    suspend fun insert(data: NegocioDto) =
        SupabaseModule.client.from("negocios").insert(data)

    suspend fun delete(id: String) =
        SupabaseModule.client.from("negocios").delete {
            filter { eq("id", id) }
        }
}
