package org.luisito.admin360.data.repository

import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order
import org.luisito.admin360.data.SupabaseClientProvider
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AuditoriaRepository {
    suspend fun registrarAccion(
        usuario: String,
        accion: String,
        entidad: String,
        entidadId: String,
        detalle: String = ""
    ): Boolean {
        return try {
            val ahora = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            val data = buildJsonObject {
                put("usuario", usuario)
                put("accion", accion)
                put("entidad", entidad)
                put("entidad_id", entidadId)
                put("detalle", detalle)
                put("created_at", ahora)
            }
            SupabaseClientProvider.client
                .postgrest.from("auditoria")
                .insert(data) {
                    select(Columns.ALL)
                }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun getAuditoria(
        limite: Int = 50,
        offset: Int = 0,
        usuario: String? = null,
        entidad: String? = null
    ): List<Map<String, Any>> {
        return try {
            SupabaseClientProvider.client
                .postgrest.from("auditoria")
                .select(Columns.ALL) {
                    order(column = "created_at", order = Order.DESCENDING)
                    limit(limite.toLong())
                    range(from = offset.toLong(), to = (offset + limite - 1).toLong())
                }
                .decodeAs<List<Map<String, Any>>>()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}
