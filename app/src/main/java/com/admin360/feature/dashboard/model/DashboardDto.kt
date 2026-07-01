package com.admin360.feature.dashboard.model

data class DashboardDto(
    val totalNegocios: Int,
    val totalLocales: Int,
    val totalUsuarios: Int,
    val licenciasActivas: Int,
    val licenciasVencidas: Int
)
