package com.gestor360.admin.domain.repository

import com.gestor360.admin.domain.models.Usuario

interface AuthRepository {

    suspend fun login(
        username: String,
        password: String
    ): Result<Usuario>
}
