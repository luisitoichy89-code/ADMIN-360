package com.gestor360.admin.data.repository

import com.gestor360.admin.domain.models.Usuario
import com.gestor360.admin.domain.repository.AuthRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.builtin.Email

class AuthRepositoryImpl(
    private val supabase: SupabaseClient
) : AuthRepository {

    override suspend fun login(
        username: String,
        password: String
    ): Result<Usuario> {

        return try {

            supabase.auth.signInWith(Email) {
                this.email = username
                this.password = password
            }

            val session = supabase.auth.currentSessionOrNull()
                ?: throw Exception("Sesión no encontrada")

            val user = supabase.auth.currentUserOrNull()
                ?: throw Exception("Usuario no encontrado")

            val usuario = Usuario(
                id = user.id,
                username = username,
                nombre = user.email ?: "",
                rol = "admin",
                clienteId = "",
                almacenId = "",
                activo = true,
                androidId = ""
            )

            Result.success(usuario)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
