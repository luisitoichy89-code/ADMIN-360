package org.luisito.admin360.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object SessionManager {

    private val _clienteId = MutableStateFlow<String?>(null)
    val clienteId: StateFlow<String?> = _clienteId.asStateFlow()

    private val _negocioId = MutableStateFlow<String?>(null)
    val negocioId: StateFlow<String?> = _negocioId.asStateFlow()

    private val _usuarioId = MutableStateFlow<String?>(null)
    val usuarioId: StateFlow<String?> = _usuarioId.asStateFlow()

    private val _rol = MutableStateFlow<String?>(null)
    val rol: StateFlow<String?> = _rol.asStateFlow()

    private val _negocioNombre = MutableStateFlow<String?>(null)
    val negocioNombre: StateFlow<String?> = _negocioNombre.asStateFlow()

    fun setSession(clienteId: String, negocioId: String, usuarioId: String, rol: String, negocioNombre: String? = null) {
        _clienteId.value = clienteId
        _negocioId.value = negocioId
        _usuarioId.value = usuarioId
        _rol.value = rol
        _negocioNombre.value = negocioNombre
    }

    fun setClienteId(clienteId: String) {
        _clienteId.value = clienteId
    }

    fun setNegocioId(negocioId: String) {
        _negocioId.value = negocioId
    }

    fun setUsuarioId(usuarioId: String) {
        _usuarioId.value = usuarioId
    }

    fun setRol(rol: String) {
        _rol.value = rol
    }

    fun setNegocioNombre(nombre: String) {
        _negocioNombre.value = nombre
    }

    fun clear() {
        _clienteId.value = null
        _negocioId.value = null
        _usuarioId.value = null
        _rol.value = null
        _negocioNombre.value = null
    }
}
