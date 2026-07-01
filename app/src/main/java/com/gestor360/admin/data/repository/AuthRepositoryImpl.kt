package com.gestor360.admin.data.repository

import com.gestor360.admin.domain.models.Usuario
import com.gestor360.admin.domain.repository.AuthRepository
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.gotrue.user.UserInfo

class AuthRepositoryImpl(
    private val supabase: Auth
) : AuthRepository {

    override suspend fun login(
        username: String,
        password: String
    ): Result<Usuario> {
        return try {

            val result = supabase.signInWith(Email) {
                this.email = username
                this.password = password
            }

            val userInfo: UserInfo? = supabase.currentUserOrNull()

            val usuario = userInfo?.let {
                Usuario(
                    id = it.id,
                    username = username,
                    nombre = it.email ?: "",
                    rol = "admin",
                    clienteId = "",
                    almacenId = "",
                    activo = true,
                    androidId = ""
                )
            } ?: throw Exception("Usuario no encontrado")

            Result.success(usuario)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
