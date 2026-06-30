package com.admin360.core.session

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object SessionManager {

    private val _session = MutableStateFlow(SessionData())
    val session = _session.asStateFlow()

    fun login(data: SessionData) {
        _session.value = data
    }

    fun logout() {
        _session.value = SessionData()
    }

    val clienteId: String
        get() = _session.value.clienteId

    val sessionValue get() = _session.value
}
