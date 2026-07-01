package com.admin360.feature.usuarios.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.admin360.feature.usuarios.data.UsuariosRepository
import com.admin360.feature.usuarios.model.UsuarioDto
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateOf

data class UsuariosState(
    val loading: Boolean = false,
    val usuarios: List<UsuarioDto> = emptyList(),
    val error: String? = null
)

class UsuariosViewModel : ViewModel() {

    private val repo = UsuariosRepository()

    var state by mutableStateOf(UsuariosState())
        private set

    fun load(negocioId: String) {
        viewModelScope.launch {
            state = state.copy(loading = true)

            val data = repo.getByNegocio(negocioId)

            state = state.copy(
                loading = false,
                usuarios = data
            )
        }
    }

    fun create(usuario: UsuarioDto, negocioId: String) {
        viewModelScope.launch {
            repo.create(usuario)
            load(negocioId)
        }
    }

    fun update(id: String, usuario: UsuarioDto, negocioId: String) {
        viewModelScope.launch {
            repo.update(id, usuario)
            load(negocioId)
        }
    }

    fun delete(id: String, negocioId: String) {
        viewModelScope.launch {
            repo.delete(id)
            load(negocioId)
        }
    }
}
