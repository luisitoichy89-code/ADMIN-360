package com.admin360.feature.locales.data

import com.admin360.core.supabase.SupabaseModule
import com.admin360.feature.locales.model.LocalDto
import io.github.jan.supabase.postgrest.from

class LocalesRepository {

    private val client = SupabaseModule.client.from("locales")

    suspend fun getByNegocio(negocioId: String): List<LocalDto> {
        return try {
            client.select {
                filter { eq("negocio_id", negocioId) }
            }.decodeList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun create(local: LocalDto): Boolean {
        return try {
            client.insert(local)
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun update(id: String, local: LocalDto): Boolean {
        return try {
            client.update({
                set("nombre", local.nombre)
                set("direccion", local.direccion)
                set("activo", local.activo)
            }) {
                filter { eq("id", id) }
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun delete(id: String): Boolean {
        return try {
            client.delete {
                filter { eq("id", id) }
            }
            true
        } catch (e: Exception) {
            false
        }
    }
}
