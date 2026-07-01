package com.gestor360.admin.domain.models

data class Producto(
    val id: String,
    val clienteId: String,
    val nombre: String,
    val precio: Double,
    val stock: Int,
    val almacenId: String
)
