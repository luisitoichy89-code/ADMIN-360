package com.gestor360.admin.domain.models

data class Usuario(
    val id: String,
    val username: String,
    val nombre: String,
    val rol: String,
    val clienteId: String,
    val almacenId: String?,
    val activo: Boolean,
    val androidId: String?
)
