package com.admin360.core.session

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class UserRole {
    SUPER_ADMIN,
    ADMIN_NEGOCIO,
    VENDEDOR
}

data class SessionData(
    val logged: Boolean = false,
    val usuarioId: String = "",
    val negocioId: String = "",
    val localId: String = "",
    val clienteId: String = "",
    val nombre: String = "",
    val email: String = "",
    val rol: UserRole = UserRole.VENDEDOR,
    val androidId: String = "",
    val licenciaActiva: Boolean = false
)

object SessionManager {

    private val _session = MutableStateFlow(SessionData())

    val session: StateFlow<SessionData> =
        _session.asStateFlow()

    fun login(data: SessionData) {
        _session.value = data
    }

    fun logout() {
        _session.value = SessionData()
    }

    val isLogged
        get() = _session.value.logged

    val clienteId
        get() = _session.value.clienteId

    val negocioId
        get() = _session.value.negocioId

    val localId
        get() = _session.value.localId

    val usuarioId
        get() = _session.value.usuarioId

    val androidId
        get() = _session.value.androidId

    val rol
        get() = _session.value.rol

    val licenciaActiva
        get() = _session.value.licenciaActiva
}
