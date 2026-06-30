package com.admin360.feature.dashboard.model

data class DashboardState(

    val negocios: Int = 0,

    val locales: Int = 0,

    val usuarios: Int = 0,

    val licenciasActivas: Int = 0,

    val loading: Boolean = false,

    val error: String? = null

)
