package com.gestor360.admin.domain.models

data class Local(
    val id: String,
    val clienteId: String,
    val nombre: String,
    val activo: Boolean,
    val createdAt: String
)
