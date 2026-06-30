package com.admin360.core.session

enum class UserRole {
    SUPER_ADMIN,
    ADMIN_NEGOCIO,
    VENDEDOR
}

data class SessionData(
    val logged: Boolean = false,
    val usuarioId: String = "",
    val negocioId: String = "",
    val clienteId: String = "",
    val nombre: String = "",
    val email: String = "",
    val rol: UserRole = UserRole.VENDEDOR,
    val androidId: String = "",
    val licenciaActiva: Boolean = false
)
