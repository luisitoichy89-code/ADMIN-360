package com.gestor360.admin.domain.models

data class Tarjeta(
    val id: String,
    val clienteId: String,
    val banco: String,
    val numero: String,
    val almacenId: String
)
