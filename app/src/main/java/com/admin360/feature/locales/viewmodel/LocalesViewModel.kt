package com.admin360.feature.locales.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.admin360.feature.locales.data.LocalesRepository
import com.admin360.feature.locales.model.LocalDto
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateOf

data class LocalesState(
    val loading: Boolean = false,
    val locales: List<LocalDto> = emptyList(),
    val error: String? = null
)

class LocalesViewModel : ViewModel() {

    private val repo = LocalesRepository()

    var state by mutableStateOf(LocalesState())
        private set

    fun load(negocioId: String) {
        viewModelScope.launch {
            state = state.copy(loading = true)

            val data = repo.getByNegocio(negocioId)

            state = state.copy(
                loading = false,
                locales = data
            )
        }
    }

    fun create(local: LocalDto) {
        viewModelScope.launch {
            repo.create(local)
            load(local.negocio_id)
        }
    }

    fun delete(id: String, negocioId: String) {
        viewModelScope.launch {
            repo.delete(id)
            load(negocioId)
        }
    }

    fun update(id: String, local: LocalDto, negocioId: String) {
        viewModelScope.launch {
            repo.update(id, local)
            load(negocioId)
        }
    }
}
