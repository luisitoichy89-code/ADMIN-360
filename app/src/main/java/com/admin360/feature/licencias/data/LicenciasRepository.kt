package com.admin360.feature.licencias.data

import com.admin360.core.supabase.SupabaseModule
import com.admin360.feature.licencias.model.LicenciaDto
import io.github.jan.supabase.postgrest.from

class LicenciasRepository {

    private val client = SupabaseModule.client.from("licencias")

    suspend fun getByNegocio(negocioId: String): List<LicenciaDto> {
        return try {
            client.select()
                .decodeList<LicenciaDto>()
                .filter { it.negocio_id == negocioId }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun create(licencia: LicenciaDto): Boolean {
        return try {
            client.insert(licencia)
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun update(id: String, licencia: LicenciaDto): Boolean {
        return try {
            client.update({
                set("estado", licencia.estado)
                set("fecha_inicio", licencia.fecha_inicio)
                set("fecha_fin", licencia.fecha_fin)
                set("android_id", licencia.android_id)
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
