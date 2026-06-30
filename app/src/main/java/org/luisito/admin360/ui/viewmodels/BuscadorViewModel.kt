package org.luisito.admin360.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.luisito.admin360.data.models.Negocio
import org.luisito.admin360.data.models.Local
import org.luisito.admin360.data.models.User
import org.luisito.admin360.data.models.Licencia
import org.luisito.admin360.data.repository.NegocioRepository
import org.luisito.admin360.data.repository.LocalRepository
import org.luisito.admin360.data.repository.UsuarioRepository
import org.luisito.admin360.data.repository.LicenciaRepository

sealed class BuscadorResultado {
    data class NegocioEncontrado(val negocio: Negocio) : BuscadorResultado()
    data class LocalEncontrado(val local: Local, val negocioNombre: String) : BuscadorResultado()
    data class UsuarioEncontrado(val usuario: User, val negocioNombre: String) : BuscadorResultado()
    data class LicenciaEncontrada(val licencia: Licencia, val negocioNombre: String) : BuscadorResultado()
    object NoEncontrado : BuscadorResultado()
}

data class BuscadorUiState(
    val isLoading: Boolean = false,
    val resultado: BuscadorResultado? = null,
    val error: String? = null,
    val codigoBuscado: String = ""
)

class BuscadorViewModel(
    private val negocioRepository: NegocioRepository = NegocioRepository(),
    private val localRepository: LocalRepository = LocalRepository(),
    private val usuarioRepository: UsuarioRepository = UsuarioRepository(),
    private val licenciaRepository: LicenciaRepository = LicenciaRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(BuscadorUiState())
    val uiState: StateFlow<BuscadorUiState> = _uiState.asStateFlow()

    fun buscar(codigo: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null, codigoBuscado = codigo) }
            
            try {
                val resultado = when {
                    // Formato: N{id} - Negocio
                    codigo.matches(Regex("^N\\d+$")) -> {
                        val id = codigo.substring(1)
                        val negocios = negocioRepository.getNegocios()
                        val negocio = negocios.find { it.id == id }
                        if (negocio != null) {
                            BuscadorResultado.NegocioEncontrado(negocio)
                        } else {
                            BuscadorResultado.NoEncontrado
                        }
                    }
                    // Formato: N{id}L{id} - Local
                    codigo.matches(Regex("^N\\d+L\\d+$")) -> {
                        val parts = codigo.split("L")
                        val negocioId = parts[0].substring(1)
                        val localId = parts[1]
                        val locales = localRepository.getLocales(negocioId)
                        val local = locales.find { it.id == localId }
                        if (local != null) {
                            val negocios = negocioRepository.getNegocios()
                            val negocio = negocios.find { it.id == negocioId }
                            BuscadorResultado.LocalEncontrado(local, negocio?.nombre_negocio ?: "Desconocido")
                        } else {
                            BuscadorResultado.NoEncontrado
                        }
                    }
                    // Formato: N{id}A{id} - Admin
                    codigo.matches(Regex("^N\\d+A\\d+$")) -> {
                        val parts = codigo.split("A")
                        val negocioId = parts[0].substring(1)
                        val usuarioId = parts[1]
                        val usuarios = usuarioRepository.getUsuarios(negocioId)
                        val usuario = usuarios.find { it.id == usuarioId && it.rol == "admin_almacen" }
                        if (usuario != null) {
                            val negocios = negocioRepository.getNegocios()
                            val negocio = negocios.find { it.id == negocioId }
                            BuscadorResultado.UsuarioEncontrado(usuario, negocio?.nombre_negocio ?: "Desconocido")
                        } else {
                            BuscadorResultado.NoEncontrado
                        }
                    }
                    // Formato: N{id}L{id}V{id} - Vendedor
                    codigo.matches(Regex("^N\\d+L\\d+V\\d+$")) -> {
                        val parts = codigo.split("L")
                        val negocioId = parts[0].substring(1)
                        val rest = parts[1].split("V")
                        val localId = rest[0]
                        val usuarioId = rest[1]
                        val usuarios = usuarioRepository.getUsuarios(negocioId)
                        val usuario = usuarios.find { 
                            it.id == usuarioId && 
                            it.rol == "seller" && 
                            it.almacen_id == localId 
                        }
                        if (usuario != null) {
                            val negocios = negocioRepository.getNegocios()
                            val negocio = negocios.find { it.id == negocioId }
                            BuscadorResultado.UsuarioEncontrado(usuario, negocio?.nombre_negocio ?: "Desconocido")
                        } else {
                            BuscadorResultado.NoEncontrado
                        }
                    }
                    // Formato: N{id}LIC{id} - Licencia
                    codigo.matches(Regex("^N\\d+LIC\\d+$")) -> {
                        val parts = codigo.split("LIC")
                        val negocioId = parts[0].substring(1)
                        val licenciaId = parts[1]
                        val licencias = licenciaRepository.getLicencias(negocioId)
                        val licencia = licencias.find { it.id == licenciaId }
                        if (licencia != null) {
                            val negocios = negocioRepository.getNegocios()
                            val negocio = negocios.find { it.id == negocioId }
                            BuscadorResultado.LicenciaEncontrada(licencia, negocio?.nombre_negocio ?: "Desconocido")
                        } else {
                            BuscadorResultado.NoEncontrado
                        }
                    }
                    else -> BuscadorResultado.NoEncontrado
                }
                
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        resultado = resultado,
                        error = if (resultado is BuscadorResultado.NoEncontrado) "No se encontró: $codigo" else null
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Error al buscar",
                        resultado = null
                    )
                }
            }
        }
    }

    fun limpiar() {
        _uiState.update { 
            it.copy(
                resultado = null,
                error = null,
                codigoBuscado = ""
            )
        }
    }
}
