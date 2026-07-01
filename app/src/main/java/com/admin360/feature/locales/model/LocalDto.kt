package com.admin360.feature.locales.model

data class LocalDto(
    val id: String? = null,
    val negocio_id: String,
    val nombre: String,
    val direccion: String? = null,
    val activo: Boolean = true
)
