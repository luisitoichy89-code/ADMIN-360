package com.admin360.feature.user.data

import com.admin360.core.supabase.SupabaseModule
import io.github.jan.supabase.postgrest.from

data class UserDto(
    val id: String,
    val email: String,
    val cliente_id: String,
    val rol: String
)

class UserRepository {

    suspend fun get(clienteId: String) =
        SupabaseModule.client.from("usuarios")
            .select { filter { eq("cliente_id", clienteId) } }
            .decodeList<UserDto>()
}
