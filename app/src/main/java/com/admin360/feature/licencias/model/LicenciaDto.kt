package com.admin360.feature.licencias.model

data class LicenciaDto(
    val id: String? = null,
    val negocio_id: String,
    val estado: String = "ACTIVA",
    val fecha_inicio: String? = null,
    val fecha_fin: String? = null,
    val android_id: String? = null
)
