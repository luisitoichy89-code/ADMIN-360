package org.luisito.admin360.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class NegocioActivoViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _negocioId = MutableStateFlow<String?>(null)
    val negocioId: StateFlow<String?> = _negocioId.asStateFlow()

    private val _negocioNombre = MutableStateFlow<String?>(null)
    val negocioNombre: StateFlow<String?> = _negocioNombre.asStateFlow()

    private val _negocioActivoCheck = MutableStateFlow<String?>(null)
    val negocioActivoCheck: StateFlow<String?> = _negocioActivoCheck.asStateFlow()

    init {
        val savedId = savedStateHandle.get<String>("negocioActivoId")
        if (savedId != null) {
            _negocioId.value = savedId
            _negocioActivoCheck.value = savedId
        }
    }

    fun seleccionarNegocio(id: String, nombre: String) {
        _negocioId.value = id
        _negocioNombre.value = nombre
        _negocioActivoCheck.value = id
        savedStateHandle.set("negocioActivoId", id)
    }

    fun limpiarNegocio() {
        _negocioId.value = null
        _negocioNombre.value = null
        _negocioActivoCheck.value = null
        savedStateHandle.remove<String>("negocioActivoId")
    }
}
