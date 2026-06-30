package org.luisito.admin360.data.repository

import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order
import org.luisito.admin360.data.SupabaseClientProvider
import org.luisito.admin360.data.models.Licencia
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import java.time.LocalDate

class LicenciaRepository(
    private val auditoriaRepository: AuditoriaRepository = AuditoriaRepository()
) {

    suspend fun getLicencias(clienteId: String): List<Licencia> {
        return try {
            SupabaseClientProvider.client
                .postgrest.from("licencias")
                .select(Columns.ALL) {
                    filter { eq("cliente_id", clienteId) }
                }
                .decodeAs<List<Licencia>>()
        } catch (e: Exception) { 
            e.printStackTrace()
            emptyList() 
        }
    }

    suspend fun getLicenciasPaginados(clienteId: String, page: Int, pageSize: Int = 10): Pair<List<Licencia>, Int> {
        return try {
            val offset = (page - 1) * pageSize
            val data = SupabaseClientProvider.client
                .postgrest.from("licencias")
                .select(Columns.ALL) {
                    filter { eq("cliente_id", clienteId) }
                    order(column = "id", order = Order.DESCENDING)
                    limit(pageSize.toLong())
                    range(from = offset.toLong(), to = (offset + pageSize - 1).toLong())
                }
                .decodeAs<List<Licencia>>()
            
            val count = SupabaseClientProvider.client
                .postgrest.from("licencias")
                .select(Columns.ALL) {
                    filter { eq("cliente_id", clienteId) }
                }
                .decodeAs<List<Licencia>>()
            
            Pair(data, count.size)
        } catch (e: Exception) { 
            e.printStackTrace()
            Pair(emptyList(), 0)
        }
    }

    suspend fun activateLicense(clienteId: String, deviceId: String, dias: Int, usuario: String = "admin"): Boolean {
        return try {
            val expiracion = LocalDate.now().plusDays(dias.toLong()).toString()
            val data = buildJsonObject {
                put("cliente_id", clienteId)
                put("device_id", deviceId)
                put("expiracion", expiracion)
                put("activo", true)
            }
            val result = SupabaseClientProvider.client
                .postgrest.from("licencias")
                .insert(data) {
                    select(Columns.ALL)
                }
                .decodeAs<List<Licencia>>()
            
            val id = result.firstOrNull()?.id ?: ""
            auditoriaRepository.registrarAccion(
                usuario = usuario,
                accion = "CREAR",
                entidad = "licencia",
                entidadId = id,
                detalle = "Licencia activada para device: $deviceId, $dias días"
            )
            true
        } catch (e: Exception) { 
            e.printStackTrace()
            false 
        }
    }

    suspend fun renewLicense(clienteId: String, dias: Int, usuario: String = "admin"): Boolean {
        return try {
            val expiracion = LocalDate.now().plusDays(dias.toLong()).toString()
            val data = buildJsonObject {
                put("expiracion", expiracion)
                put("activo", true)
            }
            SupabaseClientProvider.client
                .postgrest.from("licencias")
                .update(data) {
                    filter { eq("cliente_id", clienteId) }
                }
            
            auditoriaRepository.registrarAccion(
                usuario = usuario,
                accion = "ACTUALIZAR",
                entidad = "licencia",
                entidadId = clienteId,
                detalle = "Licencia renovada: $dias días"
            )
            true
        } catch (e: Exception) { 
            e.printStackTrace()
            false 
        }
    }

    suspend fun deleteLicense(id: String, usuario: String = "admin"): Boolean {
        return try {
            val items = SupabaseClientProvider.client
                .postgrest.from("licencias")
                .select(Columns.ALL) {
                    filter { eq("id", id) }
                }
                .decodeAs<List<Licencia>>()
            val deviceId = items.firstOrNull()?.device_id ?: id
            
            SupabaseClientProvider.client
                .postgrest.from("licencias")
                .delete {
                    filter { eq("id", id) }
                }
            
            auditoriaRepository.registrarAccion(
                usuario = usuario,
                accion = "ELIMINAR",
                entidad = "licencia",
                entidadId = id,
                detalle = "Licencia eliminada para device: $deviceId"
            )
            true
        } catch (e: Exception) { 
            e.printStackTrace()
            false 
        }
    }
}
