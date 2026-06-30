package org.luisito.admin360.data.repository

import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order
import org.luisito.admin360.data.SupabaseClientProvider
import org.luisito.admin360.data.models.User
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import java.security.MessageDigest

class UsuarioRepository(
    private val auditoriaRepository: AuditoriaRepository = AuditoriaRepository()
) {

    suspend fun getUsuarios(clienteId: String): List<User> {
        return try {
            SupabaseClientProvider.client
                .postgrest.from("usuarios")
                .select(Columns.ALL) {
                    filter { eq("cliente_id", clienteId) }
                }
                .decodeAs<List<User>>()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun getUsuariosPaginados(clienteId: String, page: Int, pageSize: Int = 10): Pair<List<User>, Int> {
        return try {
            val offset = (page - 1) * pageSize
            val data = SupabaseClientProvider.client
                .postgrest.from("usuarios")
                .select(Columns.ALL) {
                    filter { eq("cliente_id", clienteId) }
                    order(column = "id", order = Order.DESCENDING)
                    limit(pageSize.toLong())
                    range(from = offset.toLong(), to = (offset + pageSize - 1).toLong())
                }
                .decodeAs<List<User>>()
            
            val count = SupabaseClientProvider.client
                .postgrest.from("usuarios")
                .select(Columns.ALL) {
                    filter { eq("cliente_id", clienteId) }
                }
                .decodeAs<List<User>>()
            
            Pair(data, count.size)
        } catch (e: Exception) {
            e.printStackTrace()
            Pair(emptyList(), 0)
        }
    }

    suspend fun verificarAndroidId(androidId: String): Boolean {
        return try {
            val result = SupabaseClientProvider.client
                .postgrest.from("usuarios")
                .select(Columns.ALL) {
                    filter { eq("auth_id", androidId) }
                }
                .decodeAs<List<Map<String, String>>>()
            result.isNotEmpty()
        } catch (e: Exception) {
            false
        }
    }

    suspend fun createUsuario(
        username: String,
        nombre: String,
        password: String,
        rol: String,
        clienteId: String,
        almacenId: String,
        androidId: String,
        usuarioAdmin: String = "admin"
    ): Boolean {
        return try {
            if (verificarAndroidId(androidId)) {
                return false
            }
            
            val hashedPassword = hash(password)
            val data = buildJsonObject {
                put("auth_id", androidId)
                put("username", username)
                put("nombre", nombre)
                put("password", hashedPassword)
                put("rol", rol)
                put("cliente_id", clienteId)
                put("almacen_id", almacenId)
                put("activo", true)
            }
            val result = SupabaseClientProvider.client
                .postgrest.from("usuarios")
                .insert(data) {
                    select(Columns.ALL)
                }
                .decodeAs<List<User>>()
            
            val userId = result.firstOrNull()?.id ?: ""
            auditoriaRepository.registrarAccion(
                usuario = usuarioAdmin,
                accion = "CREAR",
                entidad = "usuario",
                entidadId = userId,
                detalle = "Usuario creado: $username, rol: $rol, androidId: $androidId"
            )
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun updateUsuario(
        id: String,
        username: String,
        nombre: String,
        rol: String,
        almacenId: String,
        activo: Boolean,
        usuarioAdmin: String = "admin"
    ): Boolean {
        return try {
            val data = buildJsonObject {
                put("username", username)
                put("nombre", nombre)
                put("rol", rol)
                put("almacen_id", almacenId)
                put("activo", activo)
            }
            SupabaseClientProvider.client
                .postgrest.from("usuarios")
                .update(data) {
                    filter { eq("id", id) }
                }
            
            auditoriaRepository.registrarAccion(
                usuario = usuarioAdmin,
                accion = "ACTUALIZAR",
                entidad = "usuario",
                entidadId = id,
                detalle = "Usuario actualizado: $username"
            )
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun resetPassword(id: String, usuarioAdmin: String = "admin"): Boolean {
        return try {
            val defaultPassword = hash("123456")
            val data = buildJsonObject {
                put("password", defaultPassword)
            }
            SupabaseClientProvider.client
                .postgrest.from("usuarios")
                .update(data) {
                    filter { eq("id", id) }
                }
            
            auditoriaRepository.registrarAccion(
                usuario = usuarioAdmin,
                accion = "RESET_PASSWORD",
                entidad = "usuario",
                entidadId = id,
                detalle = "Contraseña restablecida a 123456"
            )
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun deleteUsuario(id: String, usuarioAdmin: String = "admin"): Boolean {
        return try {
            val usuarios = SupabaseClientProvider.client
                .postgrest.from("usuarios")
                .select(Columns.ALL) {
                    filter { eq("id", id) }
                }
                .decodeAs<List<User>>()
            val username = usuarios.firstOrNull()?.username ?: id
            
            SupabaseClientProvider.client
                .postgrest.from("usuarios")
                .delete {
                    filter { eq("id", id) }
                }
            
            auditoriaRepository.registrarAccion(
                usuario = usuarioAdmin,
                accion = "ELIMINAR",
                entidad = "usuario",
                entidadId = id,
                detalle = "Usuario eliminado: $username"
            )
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    private fun hash(password: String): String {
        val bytes = MessageDigest.getInstance("SHA-256")
            .digest(password.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
}
