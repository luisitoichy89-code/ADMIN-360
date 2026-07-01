package com.admin360.feature.usuarios.model

data class UsuarioDto(
    val id: String? = null,
    val negocio_id: String,
    val local_id: String? = null,
    val nombre: String,
    val email: String,
    val rol: String, // ADMIN | VENDEDOR
    val activo: Boolean = true
)
