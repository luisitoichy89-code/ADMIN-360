package com.admin360.feature.negocios.data

import com.admin360.core.supabase.SupabaseModule
import com.admin360.feature.negocios.model.NegocioDto
import io.github.jan.supabase.postgrest.from

class NegociosRepository {

    private val client = SupabaseModule.client.from("negocios")

    suspend fun getAll(): List<NegocioDto> {
        return try {
            client.select().decodeList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun create(negocio: NegocioDto): Boolean {
        return try {
            client.insert(negocio)
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun update(id: String, negocio: NegocioDto): Boolean {
        return try {
            client.update({
                set("nombre", negocio.nombre)
                set("propietario", negocio.propietario)
                set("email", negocio.email)
                set("telefono", negocio.telefono)
                set("activo", negocio.activo)
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
