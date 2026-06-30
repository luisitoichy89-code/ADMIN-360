package org.luisito.admin360.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.luisito.admin360.data.SessionManager
import org.luisito.admin360.data.models.User
import org.luisito.admin360.data.repository.UsuarioRepository

data class UsuarioUiState(
    val isLoading: Boolean = false,
    val usuarios: List<User> = emptyList(),
    val error: String? = null,
    val paginaActual: Int = 1,
    val totalPaginas: Int = 1,
    val totalItems: Int = 0,
    val itemsPorPagina: Int = 10,
    val androidIdDuplicado: Boolean = false
)

class UsuarioViewModel(
    private val repository: UsuarioRepository = UsuarioRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(UsuarioUiState())
    val uiState: StateFlow<UsuarioUiState> = _uiState.asStateFlow()

    private val sessionManager = SessionManager

    fun loadUsuarios() {
        val clienteId = sessionManager.clienteId.value
        if (clienteId == null) {
            _uiState.update { it.copy(error = "No hay negocio seleccionado") }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val page = _uiState.value.paginaActual
            val pageSize = _uiState.value.itemsPorPagina
            val result = repository.getUsuariosPaginados(clienteId, page, pageSize)
            val usuarios = result.first
            val total = result.second
            val totalPaginas = if (total > 0) (total + pageSize - 1) / pageSize else 1
            _uiState.update {
                it.copy(
                    isLoading = false,
                    usuarios = usuarios,
                    error = if (usuarios.isEmpty() && total == 0) "No hay usuarios" else null,
                    totalItems = total,
                    totalPaginas = totalPaginas,
                    androidIdDuplicado = false
                )
            }
        }
    }

    fun siguientePagina() {
        val current = _uiState.value.paginaActual
        val total = _uiState.value.totalPaginas
        if (current < total) {
            _uiState.update { it.copy(paginaActual = current + 1) }
            loadUsuarios()
        }
    }

    fun paginaAnterior() {
        val current = _uiState.value.paginaActual
        if (current > 1) {
            _uiState.update { it.copy(paginaActual = current - 1) }
            loadUsuarios()
        }
    }

    suspend fun verificarAndroidId(androidId: String): Boolean {
        return repository.verificarAndroidId(androidId)
    }

    fun createUsuario(
        username: String,
        nombre: String,
        password: String,
        rol: String,
        almacenId: String,
        androidId: String
    ) {
        val clienteId = sessionManager.clienteId.value
        if (clienteId == null) {
            _uiState.update { it.copy(error = "No hay negocio seleccionado") }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, androidIdDuplicado = false) }
            
            val existe = repository.verificarAndroidId(androidId)
            if (existe) {
                _uiState.update { 
                    it.copy(
                        isLoading = false, 
                        error = "Este Android ID ya está registrado",
                        androidIdDuplicado = true
                    )
                }
                return@launch
            }
            
            val success = repository.createUsuario(username, nombre, password, rol, clienteId, almacenId, androidId)
            if (success) {
                _uiState.update { it.copy(paginaActual = 1) }
                loadUsuarios()
            } else {
                _uiState.update { it.copy(isLoading = false, error = "Error al crear usuario") }
            }
        }
    }

    fun updateUsuario(
        id: String,
        username: String,
        nombre: String,
        rol: String,
        almacenId: String,
        activo: Boolean
    ) {
        val clienteId = sessionManager.clienteId.value
        if (clienteId == null) {
            _uiState.update { it.copy(error = "No hay negocio seleccionado") }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val success = repository.updateUsuario(id, username, nombre, rol, almacenId, activo)
            if (success) {
                loadUsuarios()
            } else {
                _uiState.update { it.copy(isLoading = false, error = "Error al actualizar usuario") }
            }
        }
    }

    fun resetPassword(id: String) {
        val clienteId = sessionManager.clienteId.value
        if (clienteId == null) {
            _uiState.update { it.copy(error = "No hay negocio seleccionado") }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val success = repository.resetPassword(id)
            if (success) {
                loadUsuarios()
            } else {
                _uiState.update { it.copy(isLoading = false, error = "Error al resetear contraseña") }
            }
        }
    }

    fun deleteUsuario(id: String) {
        val clienteId = sessionManager.clienteId.value
        if (clienteId == null) {
            _uiState.update { it.copy(error = "No hay negocio seleccionado") }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val success = repository.deleteUsuario(id)
            if (success) {
                loadUsuarios()
            } else {
                _uiState.update { it.copy(isLoading = false, error = "Error al eliminar usuario") }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null, androidIdDuplicado = false) }
    }
}
