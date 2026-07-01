package com.admin360.feature.negocios.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.admin360.feature.negocios.data.NegociosRepository
import com.admin360.feature.negocios.model.NegocioDto
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateOf

data class NegociosState(
    val loading: Boolean = false,
    val negocios: List<NegocioDto> = emptyList(),
    val error: String? = null
)

class NegociosViewModel : ViewModel() {

    private val repo = NegociosRepository()

    var state by mutableStateOf(NegociosState())
        private set

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            state = state.copy(loading = true)

            val data = repo.getAll()

            state = state.copy(
                loading = false,
                negocios = data
            )
        }
    }

    fun create(negocio: NegocioDto) {
        viewModelScope.launch {
            state = state.copy(loading = true)

            val ok = repo.create(negocio)

            if (ok) load()
            else state = state.copy(error = "Error creando negocio")
        }
    }

    fun delete(id: String) {
        viewModelScope.launch {
            repo.delete(id)
            load()
        }
    }

    fun update(id: String, negocio: NegocioDto) {
        viewModelScope.launch {
            repo.update(id, negocio)
            load()
        }
    }
}
