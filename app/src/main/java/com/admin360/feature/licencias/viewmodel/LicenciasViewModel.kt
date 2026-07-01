package com.admin360.feature.licencias.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.admin360.feature.licencias.data.LicenciasRepository
import com.admin360.feature.licencias.model.LicenciaDto
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateOf

data class LicenciasState(
    val loading: Boolean = false,
    val licencias: List<LicenciaDto> = emptyList(),
    val error: String? = null
)

class LicenciasViewModel : ViewModel() {

    private val repo = LicenciasRepository()

    var state by mutableStateOf(LicenciasState())
        private set

    fun load(negocioId: String) {
        viewModelScope.launch {
            state = state.copy(loading = true)

            val data = repo.getByNegocio(negocioId)

            state = state.copy(
                loading = false,
                licencias = data
            )
        }
    }

    fun create(licencia: LicenciaDto, negocioId: String) {
        viewModelScope.launch {
            repo.create(licencia)
            load(negocioId)
        }
    }

    fun update(id: String, licencia: LicenciaDto, negocioId: String) {
        viewModelScope.launch {
            repo.update(id, licencia)
            load(negocioId)
        }
    }

    fun delete(id: String, negocioId: String) {
        viewModelScope.launch {
            repo.delete(id)
            load(negocioId)
        }
    }

    fun getActive(negocioId: String): LicenciaDto? {
        var result: LicenciaDto? = null
        viewModelScope.launch {
            result = repo.getActiveByNegocio(negocioId)
        }
        return result
    }
}
