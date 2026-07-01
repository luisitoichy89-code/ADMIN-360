package com.admin360.feature.usuarios.data

import com.admin360.core.supabase.SupabaseModule
import com.admin360.feature.usuarios.model.UsuarioDto
import io.github.jan.supabase.postgrest.from

class UsuariosRepository {

    private val client = SupabaseModule.client.from("usuarios")

    suspend fun getByNegocio(negocioId: String): List<UsuarioDto> {
        return try {
            client.select()
                .decodeList<UsuarioDto>()
                .filter { it.negocio_id == negocioId }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun create(usuario: UsuarioDto): Boolean {
        return try {
            client.insert(usuario)
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun update(id: String, usuario: UsuarioDto): Boolean {
        return try {
            client.update({
                set("nombre", usuario.nombre)
                set("email", usuario.email)
                set("rol", usuario.rol)
                set("local_id", usuario.local_id)
                set("activo", usuario.activo)
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
