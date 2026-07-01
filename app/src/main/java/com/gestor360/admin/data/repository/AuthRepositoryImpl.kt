package com.gestor360.admin.data.repository

import com.gestor360.admin.domain.models.Usuario
import com.gestor360.admin.domain.repository.AuthRepository
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.createSupabaseClient

class AuthRepositoryImpl(
    private val supabase: Auth
) : AuthRepository {

    override suspend fun login(
        username: String,
        password: String
    ): Result<Usuario> {
        return try {

            val response = supabase.signInWith(Email) {
                email = username
                this.password = password
            }

            val session = supabase.currentSessionOrNull()
                ?: return Result.failure(Exception("No session available"))

            val user = session.user

            val usuario = Usuario(
                id = user.id,
                username = user.email ?: username,
                nombre = user.userMetadata?.get("nombre")?.toString() ?: "",
                rol = user.userMetadata?.get("rol")?.toString() ?: "seller",
                clienteId = user.userMetadata?.get("clienteId")?.toString() ?: "",
                almacenId = user.userMetadata?.get("almacenId")?.toString(),
                activo = true,
                androidId = user.userMetadata?.get("androidId")?.toString()
            )

            Result.success(usuario)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
