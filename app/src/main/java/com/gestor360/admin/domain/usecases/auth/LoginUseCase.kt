package com.gestor360.admin.domain.usecases.auth

import com.gestor360.admin.domain.models.Usuario
import com.gestor360.admin.domain.repository.AuthRepository

class LoginUseCase(
    private val authRepository: AuthRepository
) {

    suspend operator fun invoke(
        username: String,
        password: String
    ): Result<Usuario> {

        if (username.isBlank()) {
            return Result.failure(
                IllegalArgumentException("El usuario es obligatorio.")
            )
        }

        if (password.isBlank()) {
            return Result.failure(
                IllegalArgumentException("La contraseña es obligatoria.")
            )
        }

        return try {
            authRepository.login(
                username = username.trim(),
                password = password
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
