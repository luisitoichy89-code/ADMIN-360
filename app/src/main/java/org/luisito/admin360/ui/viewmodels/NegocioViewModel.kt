package org.luisito.admin360.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.luisito.admin360.data.models.Negocio
import org.luisito.admin360.data.repository.ErrorHolder
import org.luisito.admin360.data.repository.NegocioRepository

data class NegocioUiState(
    val isLoading: Boolean = false,
    val negocios: List<Negocio> = emptyList(),
    val error: String? = null,
    val paginaActual: Int = 1,
    val totalPaginas: Int = 1,
    val totalItems: Int = 0,
    val itemsPorPagina: Int = 10
)

class NegocioViewModel(
    private val repository: NegocioRepository = NegocioRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(NegocioUiState())
    val uiState: StateFlow<NegocioUiState> = _uiState.asStateFlow()

    fun loadNegocios() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val page = _uiState.value.paginaActual
            val pageSize = _uiState.value.itemsPorPagina
            val (negocios, total) = repository.getNegociosPaginados(page, pageSize)
            val totalPaginas = if (total > 0) (total + pageSize - 1) / pageSize else 1
            _uiState.update {
                it.copy(
                    isLoading = false,
                    negocios = negocios,
                    error = if (negocios.isEmpty() && total == 0) "No hay negocios" else null,
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
            loadNegocios()
        }
    }

    fun paginaAnterior() {
        val current = _uiState.value.paginaActual
        if (current > 1) {
            _uiState.update { it.copy(paginaActual = current - 1) }
            loadNegocios()
        }
    }

    fun irAPagina(pagina: Int) {
        val total = _uiState.value.totalPaginas
        val nueva = pagina.coerceIn(1, total)
        if (nueva != _uiState.value.paginaActual) {
            _uiState.update { it.copy(paginaActual = nueva) }
            loadNegocios()
        }
    }

    fun createNegocio(nombre: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val success = repository.createNegocio(nombre)
            if (success) {
                _uiState.update { it.copy(paginaActual = 1) }
                loadNegocios()
            } else {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = ErrorHolder.lastError
                    )
                }
            }
        }
    }

    fun updateNegocio(id: String, nombre: String, activo: Boolean) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val success = repository.updateNegocio(id, nombre, activo)
            if (success) {
                loadNegocios()
            } else {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Error al actualizar negocio"
                    )
                }
            }
        }
    }

    fun deleteNegocio(id: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val success = repository.deleteNegocio(id)
            if (success) {
                loadNegocios()
            } else {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Error al eliminar negocio"
                    )
                }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}
