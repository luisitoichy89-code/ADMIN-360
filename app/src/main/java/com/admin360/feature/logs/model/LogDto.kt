package com.admin360.feature.logs.model

data class LogDto(
    val accion: String,
    val created_at: String,
    val usuario: String? = null
)
