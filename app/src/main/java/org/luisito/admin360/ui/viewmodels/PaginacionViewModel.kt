package org.luisito.admin360.ui.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class PaginacionState(
    val paginaActual: Int = 1,
    val totalItems: Int = 0,
    val itemsPorPagina: Int = 10,
    val totalPaginas: Int = 0
)

class PaginacionViewModel : ViewModel() {

    private val _state = MutableStateFlow(PaginacionState())
    val state: StateFlow<PaginacionState> = _state.asStateFlow()

    fun setTotalItems(total: Int) {
        val itemsPorPagina = _state.value.itemsPorPagina
        val totalPaginas = if (total > 0) {
            (total + itemsPorPagina - 1) / itemsPorPagina
        } else {
            1
        }
        _state.value = _state.value.copy(
            totalItems = total,
            totalPaginas = totalPaginas,
            paginaActual = if (_state.value.paginaActual > totalPaginas) totalPaginas else _state.value.paginaActual
        )
    }

    fun siguientePagina() {
        val max = _state.value.totalPaginas
        if (_state.value.paginaActual < max) {
            _state.value = _state.value.copy(paginaActual = _state.value.paginaActual + 1)
        }
    }

    fun paginaAnterior() {
        if (_state.value.paginaActual > 1) {
            _state.value = _state.value.copy(paginaActual = _state.value.paginaActual - 1)
        }
    }

    fun irAPagina(pagina: Int) {
        val max = _state.value.totalPaginas
        val nueva = if (pagina < 1) 1 else if (pagina > max) max else pagina
        _state.value = _state.value.copy(paginaActual = nueva)
    }

    fun reset() {
        _state.value = PaginacionState()
    }
}
