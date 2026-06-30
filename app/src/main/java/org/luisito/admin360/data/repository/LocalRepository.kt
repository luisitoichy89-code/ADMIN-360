package org.luisito.admin360.data.repository

import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order
import org.luisito.admin360.data.SupabaseClientProvider
import org.luisito.admin360.data.models.Local
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class LocalRepository(
    private val auditoriaRepository: AuditoriaRepository = AuditoriaRepository()
) {

    suspend fun getLocales(clienteId: String): List<Local> {
        return try {
            SupabaseClientProvider.client
                .postgrest.from("locales")
                .select(Columns.ALL) {
                    filter { eq("cliente_id", clienteId) }
                }
                .decodeAs<List<Local>>()
        } catch (e: Exception) { 
            e.printStackTrace()
            emptyList() 
        }
    }

    suspend fun getLocalesPaginados(clienteId: String, page: Int, pageSize: Int = 10): Pair<List<Local>, Int> {
        return try {
            val offset = (page - 1) * pageSize
            val data = SupabaseClientProvider.client
                .postgrest.from("locales")
                .select(Columns.ALL) {
                    filter { eq("cliente_id", clienteId) }
                    order(column = "id", order = Order.DESCENDING)
                    limit(pageSize.toLong())
                    range(from = offset.toLong(), to = (offset + pageSize - 1).toLong())
                }
                .decodeAs<List<Local>>()
            
            val count = SupabaseClientProvider.client
                .postgrest.from("locales")
                .select(Columns.ALL) {
                    filter { eq("cliente_id", clienteId) }
                }
                .decodeAs<List<Local>>()
            
            Pair(data, count.size)
        } catch (e: Exception) { 
            e.printStackTrace()
            Pair(emptyList(), 0)
        }
    }

    suspend fun createLocal(clienteId: String, nombre: String, ruc: String, direccion: String, usuario: String = "admin"): Boolean {
        return try {
            val data = buildJsonObject {
                put("cliente_id", clienteId)
                put("nombre", nombre)
                put("ruc", ruc)
                put("direccion", direccion)
                put("activo", true)
            }
            val result = SupabaseClientProvider.client
                .postgrest.from("locales")
                .insert(data) {
                    select(Columns.ALL)
                }
                .decodeAs<List<Local>>()
            
            val id = result.firstOrNull()?.id ?: ""
            auditoriaRepository.registrarAccion(
                usuario = usuario,
                accion = "CREAR",
                entidad = "local",
                entidadId = id,
                detalle = "Local creado: $nombre"
            )
            true
        } catch (e: Exception) { 
            e.printStackTrace()
            false 
        }
    }

    suspend fun updateLocal(id: String, nombre: String, ruc: String, direccion: String, activo: Boolean, usuario: String = "admin"): Boolean {
        return try {
            val data = buildJsonObject {
                put("nombre", nombre)
                put("ruc", ruc)
                put("direccion", direccion)
                put("activo", activo)
            }
            SupabaseClientProvider.client
                .postgrest.from("locales")
                .update(data) {
                    filter { eq("id", id) }
                }
            
            auditoriaRepository.registrarAccion(
                usuario = usuario,
                accion = "ACTUALIZAR",
                entidad = "local",
                entidadId = id,
                detalle = "Local actualizado: $nombre"
            )
            true
        } catch (e: Exception) { 
            e.printStackTrace()
            false 
        }
    }

    suspend fun deleteLocal(id: String, usuario: String = "admin"): Boolean {
        return try {
            val items = SupabaseClientProvider.client
                .postgrest.from("locales")
                .select(Columns.ALL) {
                    filter { eq("id", id) }
                }
                .decodeAs<List<Local>>()
            val nombre = items.firstOrNull()?.nombre ?: id
            
            SupabaseClientProvider.client
                .postgrest.from("locales")
                .delete {
                    filter { eq("id", id) }
                }
            
            auditoriaRepository.registrarAccion(
                usuario = usuario,
                accion = "ELIMINAR",
                entidad = "local",
                entidadId = id,
                detalle = "Local eliminado: $nombre"
            )
            true
        } catch (e: Exception) { 
            e.printStackTrace()
            false 
        }
    }
}
