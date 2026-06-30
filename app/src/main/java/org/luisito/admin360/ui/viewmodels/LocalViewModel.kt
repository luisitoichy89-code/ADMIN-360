package org.luisito.admin360.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.luisito.admin360.data.SessionManager
import org.luisito.admin360.data.models.Local
import org.luisito.admin360.data.repository.LocalRepository

data class LocalUiState(
    val isLoading: Boolean = false,
    val locales: List<Local> = emptyList(),
    val error: String? = null,
    val paginaActual: Int = 1,
    val totalPaginas: Int = 1,
    val totalItems: Int = 0,
    val itemsPorPagina: Int = 10
)

class LocalViewModel(
    private val repository: LocalRepository = LocalRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(LocalUiState())
    val uiState: StateFlow<LocalUiState> = _uiState.asStateFlow()

    private val sessionManager = SessionManager

    fun loadLocales() {
        val clienteId = sessionManager.clienteId.value
        if (clienteId == null) {
            _uiState.update { it.copy(error = "No hay negocio seleccionado") }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val page = _uiState.value.paginaActual
            val pageSize = _uiState.value.itemsPorPagina
            val result = repository.getLocalesPaginados(clienteId, page, pageSize)
            val locales = result.first
            val total = result.second
            val totalPaginas = if (total > 0) (total + pageSize - 1) / pageSize else 1
            _uiState.update {
                it.copy(
                    isLoading = false,
                    locales = locales,
                    error = if (locales.isEmpty() && total == 0) "No hay locales" else null,
                    totalItems = total,
                    totalPaginas = totalPaginas
                )
            }
        }
    }

    fun siguientePagina() {
        val current = _uiState.value.paginaActual
        val total = _uiState.value.totalPaginas
        if (current < total) {
            _uiState.update { it.copy(paginaActual = current + 1) }
            loadLocales()
        }
    }

    fun paginaAnterior() {
        val current = _uiState.value.paginaActual
        if (current > 1) {
            _uiState.update { it.copy(paginaActual = current - 1) }
            loadLocales()
        }
    }

    fun createLocal(nombre: String, ruc: String, direccion: String) {
        val clienteId = sessionManager.clienteId.value
        if (clienteId == null) {
            _uiState.update { it.copy(error = "No hay negocio seleccionado") }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val success = repository.createLocal(clienteId, nombre, ruc, direccion)
            if (success) {
                _uiState.update { it.copy(paginaActual = 1) }
                loadLocales()
            } else {
                _uiState.update { it.copy(isLoading = false, error = "Error al crear local") }
            }
        }
    }

    fun updateLocal(id: String, nombre: String, ruc: String, direccion: String, activo: Boolean) {
        val clienteId = sessionManager.clienteId.value
        if (clienteId == null) {
            _uiState.update { it.copy(error = "No hay negocio seleccionado") }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val success = repository.updateLocal(id, nombre, ruc, direccion, activo)
            if (success) {
                loadLocales()
            } else {
                _uiState.update { it.copy(isLoading = false, error = "Error al actualizar") }
            }
        }
    }

    fun deleteLocal(id: String) {
        val clienteId = sessionManager.clienteId.value
        if (clienteId == null) {
            _uiState.update { it.copy(error = "No hay negocio seleccionado") }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val success = repository.deleteLocal(id)
            if (success) {
                loadLocales()
            } else {
                _uiState.update { it.copy(isLoading = false, error = "Error al eliminar") }
            }
        }
    }
}
