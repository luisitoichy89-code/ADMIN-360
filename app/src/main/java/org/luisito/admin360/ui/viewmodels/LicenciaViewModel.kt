package org.luisito.admin360.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.luisito.admin360.data.SessionManager
import org.luisito.admin360.data.models.Licencia
import org.luisito.admin360.data.repository.LicenciaRepository

data class LicenciaUiState(
    val isLoading: Boolean = false,
    val licencias: List<Licencia> = emptyList(),
    val error: String? = null,
    val paginaActual: Int = 1,
    val totalPaginas: Int = 1,
    val totalItems: Int = 0,
    val itemsPorPagina: Int = 10
)

class LicenciaViewModel(
    private val repository: LicenciaRepository = LicenciaRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(LicenciaUiState())
    val uiState: StateFlow<LicenciaUiState> = _uiState.asStateFlow()

    private val sessionManager = SessionManager

    fun loadLicencias() {
        val clienteId = sessionManager.clienteId.value
        if (clienteId == null) {
            _uiState.update { it.copy(error = "No hay negocio seleccionado") }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val page = _uiState.value.paginaActual
            val pageSize = _uiState.value.itemsPorPagina
            val result = repository.getLicenciasPaginados(clienteId, page, pageSize)
            val licencias = result.first
            val total = result.second
            val totalPaginas = if (total > 0) (total + pageSize - 1) / pageSize else 1
            _uiState.update {
                it.copy(
                    isLoading = false,
                    licencias = licencias,
                    error = if (licencias.isEmpty() && total == 0) "No hay licencias" else null,
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
            loadLicencias()
        }
    }

    fun paginaAnterior() {
        val current = _uiState.value.paginaActual
        if (current > 1) {
            _uiState.update { it.copy(paginaActual = current - 1) }
            loadLicencias()
        }
    }

    fun activateLicense(deviceId: String, dias: Int) {
        val clienteId = sessionManager.clienteId.value
        if (clienteId == null) {
            _uiState.update { it.copy(error = "No hay negocio seleccionado") }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val success = repository.activateLicense(clienteId, deviceId, dias)
            if (success) {
                _uiState.update { it.copy(paginaActual = 1) }
                loadLicencias()
            } else {
                _uiState.update { it.copy(isLoading = false, error = "Error al activar licencia") }
            }
        }
    }

    fun renewLicense(dias: Int) {
        val clienteId = sessionManager.clienteId.value
        if (clienteId == null) {
            _uiState.update { it.copy(error = "No hay negocio seleccionado") }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val success = repository.renewLicense(clienteId, dias)
            if (success) {
                loadLicencias()
            } else {
                _uiState.update { it.copy(isLoading = false, error = "Error al renovar licencia") }
            }
        }
    }

    fun deleteLicense(id: String) {
        val clienteId = sessionManager.clienteId.value
        if (clienteId == null) {
            _uiState.update { it.copy(error = "No hay negocio seleccionado") }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val success = repository.deleteLicense(id)
            if (success) {
                loadLicencias()
            } else {
                _uiState.update { it.copy(isLoading = false, error = "Error al eliminar licencia") }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}
