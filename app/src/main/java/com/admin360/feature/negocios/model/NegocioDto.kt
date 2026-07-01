package com.admin360.feature.negocios.model

data class NegocioDto(
    val id: String? = null,
    val nombre: String,
    val propietario: String,
    val email: String? = null,
    val telefono: String? = null,
    val activo: Boolean = true
)
