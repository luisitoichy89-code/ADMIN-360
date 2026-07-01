package com.gestor360.admin.domain.models

data class Licencia(
    val id: String,
    val clienteId: String,
    val deviceId: String,
    val expiracion: String,
    val activo: Boolean,
    val createdAt: String
)
