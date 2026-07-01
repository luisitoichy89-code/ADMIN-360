package com.gestor360.admin.domain.models

data class Venta(
    val id: String,
    val clienteId: String,
    val productoId: String,
    val cantidad: Int,
    val total: Double,
    val metodo: String,
    val efectivo: Double,
    val transferencia: Double,
    val usuarioId: String,
    val almacenId: String,
    val clienteCi: String,
    val clienteTel: String,
    val clienteNombre: String,
    val createdAt: String
)
