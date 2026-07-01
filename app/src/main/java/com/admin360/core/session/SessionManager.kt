package com.admin360.core.session

import com.admin360.feature.usuarios.model.UsuarioDto

object SessionManager {

    var user: UsuarioDto? = null
    var negocioId: String? = null
    var licenciaActiva: Boolean = false
    var deviceId: String? = null

    fun clear() {
        user = null
        negocioId = null
        licenciaActiva = false
        deviceId = null
    }
}
