package com.admin360.data.repository

import com.admin360.core.supabase.SupabaseModule
import io.github.jan.supabase.postgrest.from

class DeviceRepository {

    suspend fun blockUser(userId: String) {
        SupabaseModule.client.from("usuarios")
            .update({
                set("android_id", null)
            }) {
                filter { eq("id", userId) }
            }
    }

    suspend fun getDevices(clienteId: String) =
        SupabaseModule.client.from("usuarios")
            .select {
                filter { eq("cliente_id", clienteId) }
            }
            .decodeList<Map<String, Any>>()
}
