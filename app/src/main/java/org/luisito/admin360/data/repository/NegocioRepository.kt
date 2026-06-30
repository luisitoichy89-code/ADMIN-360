package org.luisito.admin360.data.repository

import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order
import org.luisito.admin360.data.SupabaseClientProvider
import org.luisito.admin360.data.models.Negocio
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

object ErrorHolder {
    var lastError: String = ""
}

class NegocioRepository(
    private val auditoriaRepository: AuditoriaRepository = AuditoriaRepository()
) {
    suspend fun getNegocios(): List<Negocio> {
        return try {
            SupabaseClientProvider.client
                .postgrest.from("clientes")
                .select(Columns.ALL)
                .decodeAs<List<Negocio>>()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun getNegociosPaginados(page: Int, pageSize: Int = 10): Pair<List<Negocio>, Int> {
        return try {
            val from = ((page - 1) * pageSize).toLong()
            val to = (from + pageSize - 1).toLong()
            val data = SupabaseClientProvider.client
                .postgrest.from("clientes")
                .select(Columns.ALL) {
                    order(column = "id", order = Order.DESCENDING)
                    range(from = from, to = to)
                }
                .decodeAs<List<Negocio>>()
            
            val count = SupabaseClientProvider.client
                .postgrest.from("clientes")
                .select(Columns.ALL)
                .decodeAs<List<Negocio>>()
            
            Pair(data, count.size)
        } catch (e: Exception) {
            e.printStackTrace()
            Pair(emptyList(), 0)
        }
    }

    suspend fun createNegocio(nombre: String, usuario: String = "admin"): Boolean {
        return try {
            val data = buildJsonObject {
                put("nombre_negocio", nombre)
                put("activo", true)
            }
            val result = SupabaseClientProvider.client
                .postgrest.from("clientes")
                .insert(data) {
                    select(Columns.ALL)
                }
                .decodeAs<List<Negocio>>()
            
            val negocioId = result.firstOrNull()?.id ?: ""
            
            auditoriaRepository.registrarAccion(
                usuario = usuario,
                accion = "CREAR",
                entidad = "negocio",
                entidadId = negocioId,
                detalle = "Negocio creado: $nombre"
            )
            true
        } catch (e: Exception) {
            e.printStackTrace()
            ErrorHolder.lastError = e.message ?: "Error desconocido"
            false
        }
    }

    suspend fun updateNegocio(id: String, nombre: String, activo: Boolean, usuario: String = "admin"): Boolean {
        return try {
            val data = buildJsonObject {
                put("nombre_negocio", nombre)
                put("activo", activo)
            }
            SupabaseClientProvider.client
                .postgrest.from("clientes")
                .update(data) {
                    filter { eq("id", id) }
                }
            
            auditoriaRepository.registrarAccion(
                usuario = usuario,
                accion = "ACTUALIZAR",
                entidad = "negocio",
                entidadId = id,
                detalle = "Negocio actualizado: $nombre, activo: $activo"
            )
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun deleteNegocio(id: String, usuario: String = "admin"): Boolean {
        return try {
            val negocios = SupabaseClientProvider.client
                .postgrest.from("clientes")
                .select(Columns.ALL) {
                    filter { eq("id", id) }
                }
                .decodeAs<List<Negocio>>()
            val nombre = negocios.firstOrNull()?.nombre_negocio ?: id
            
            SupabaseClientProvider.client
                .postgrest.from("clientes")
                .delete {
                    filter { eq("id", id) }
                }
            
            auditoriaRepository.registrarAccion(
                usuario = usuario,
                accion = "ELIMINAR",
                entidad = "negocio",
                entidadId = id,
                detalle = "Negocio eliminado: $nombre"
            )
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
