package org.luisito.admin360.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.luisito.admin360.data.repository.NegocioRepository
import org.luisito.admin360.data.repository.LocalRepository
import org.luisito.admin360.data.repository.UsuarioRepository
import org.luisito.admin360.data.repository.LicenciaRepository

data class SucursalForm(
    val nombre: String = "",
    val ruc: String = "",
    val direccion: String = ""
)

data class VendedorForm(
    val nombre: String = "",
    val usuario: String = "",
    val password: String = "",
    val androidId: String = "",
    val localSeleccionado: String = ""
)

data class RegistroClienteUiState(
    val isLoading: Boolean = false,
    val negocioNombre: String = "",
    val adminNombre: String = "",
    val adminUsername: String = "",
    val adminAndroidId: String = "",
    val cantidadSucursales: Int = 1,
    val sucursales: List<SucursalForm> = listOf(SucursalForm()),
    val cantidadVendedores: Int = 0,
    val vendedores: List<VendedorForm> = emptyList(),
    val diasLicencia: Int = 30,
    val error: String? = null,
    val success: Boolean = false
)

class RegistroClienteViewModel(
    private val negocioRepository: NegocioRepository = NegocioRepository(),
    private val localRepository: LocalRepository = LocalRepository(),
    private val usuarioRepository: UsuarioRepository = UsuarioRepository(),
    private val licenciaRepository: LicenciaRepository = LicenciaRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegistroClienteUiState())
    val uiState: StateFlow<RegistroClienteUiState> = _uiState.asStateFlow()

    fun updateNegocioNombre(nombre: String) {
        _uiState.update { it.copy(negocioNombre = nombre) }
    }

    fun updateAdminNombre(nombre: String) {
        _uiState.update { it.copy(adminNombre = nombre) }
    }

    fun updateAdminUsername(username: String) {
        _uiState.update { it.copy(adminUsername = username) }
    }

    fun updateAdminAndroidId(id: String) {
        _uiState.update { it.copy(adminAndroidId = id) }
    }

    fun updateCantidadSucursales(cantidad: Int) {
        val nuevaCantidad = cantidad.coerceAtLeast(1)
        val nuevasSucursales = if (nuevaCantidad > _uiState.value.sucursales.size) {
            _uiState.value.sucursales + List(nuevaCantidad - _uiState.value.sucursales.size) { SucursalForm() }
        } else {
            _uiState.value.sucursales.take(nuevaCantidad)
        }
        _uiState.update { 
            it.copy(
                cantidadSucursales = nuevaCantidad,
                sucursales = nuevasSucursales
            )
        }
    }

    fun updateSucursal(index: Int, nombre: String, ruc: String, direccion: String) {
        val nuevasSucursales = _uiState.value.sucursales.toMutableList()
        nuevasSucursales[index] = SucursalForm(nombre, ruc, direccion)
        _uiState.update { it.copy(sucursales = nuevasSucursales) }
    }

    fun updateCantidadVendedores(cantidad: Int) {
        val nuevaCantidad = cantidad.coerceAtLeast(0)
        val nuevosVendedores = if (nuevaCantidad > _uiState.value.vendedores.size) {
            _uiState.value.vendedores + List(nuevaCantidad - _uiState.value.vendedores.size) { VendedorForm() }
        } else {
            _uiState.value.vendedores.take(nuevaCantidad)
        }
        _uiState.update { 
            it.copy(
                cantidadVendedores = nuevaCantidad,
                vendedores = nuevosVendedores
            )
        }
    }

    fun updateVendedor(index: Int, nombre: String, usuario: String, password: String, androidId: String, localId: String) {
        val nuevosVendedores = _uiState.value.vendedores.toMutableList()
        nuevosVendedores[index] = VendedorForm(nombre, usuario, password, androidId, localId)
        _uiState.update { it.copy(vendedores = nuevosVendedores) }
    }

    fun updateDiasLicencia(dias: Int) {
        _uiState.update { it.copy(diasLicencia = dias.coerceAtLeast(1)) }
    }

    suspend fun verificarAndroidId(androidId: String): Boolean {
        return usuarioRepository.verificarAndroidId(androidId)
    }

    fun registrarCliente() {
        viewModelScope.launch {
            val state = _uiState.value
            
            if (state.negocioNombre.isBlank()) {
                _uiState.update { it.copy(error = "El nombre del negocio es obligatorio") }
                return@launch
            }
            if (state.adminNombre.isBlank()) {
                _uiState.update { it.copy(error = "El nombre del administrador es obligatorio") }
                return@launch
            }
            if (state.adminUsername.isBlank()) {
                _uiState.update { it.copy(error = "El usuario del administrador es obligatorio") }
                return@launch
            }
            if (state.adminAndroidId.isBlank()) {
                _uiState.update { it.copy(error = "El Android ID del administrador es obligatorio") }
                return@launch
            }

            val adminIdExiste = usuarioRepository.verificarAndroidId(state.adminAndroidId)
            if (adminIdExiste) {
                _uiState.update { it.copy(error = "El Android ID del administrador ya está registrado") }
                return@launch
            }

            for ((index, sucursal) in state.sucursales.withIndex()) {
                if (sucursal.nombre.isBlank()) {
                    _uiState.update { it.copy(error = "El nombre de la sucursal ${index + 1} es obligatorio") }
                    return@launch
                }
                if (sucursal.ruc.isBlank()) {
                    _uiState.update { it.copy(error = "El RUC de la sucursal ${index + 1} es obligatorio") }
                    return@launch
                }
                if (sucursal.direccion.isBlank()) {
                    _uiState.update { it.copy(error = "La dirección de la sucursal ${index + 1} es obligatoria") }
                    return@launch
                }
            }

            for ((index, vendedor) in state.vendedores.withIndex()) {
                if (vendedor.nombre.isBlank()) {
                    _uiState.update { it.copy(error = "El nombre del vendedor ${index + 1} es obligatorio") }
                    return@launch
                }
                if (vendedor.usuario.isBlank()) {
                    _uiState.update { it.copy(error = "El usuario del vendedor ${index + 1} es obligatorio") }
                    return@launch
                }
                if (vendedor.password.isBlank()) {
                    _uiState.update { it.copy(error = "La contraseña del vendedor ${index + 1} es obligatoria") }
                    return@launch
                }
                if (vendedor.androidId.isBlank()) {
                    _uiState.update { it.copy(error = "El Android ID del vendedor ${index + 1} es obligatorio") }
                    return@launch
                }
                if (vendedor.localSeleccionado.isBlank()) {
                    _uiState.update { it.copy(error = "Debes asignar un local al vendedor ${index + 1}") }
                    return@launch
                }

                val vendedorIdExiste = usuarioRepository.verificarAndroidId(vendedor.androidId)
                if (vendedorIdExiste) {
                    _uiState.update { it.copy(error = "El Android ID del vendedor ${index + 1} ya está registrado") }
                    return@launch
                }
            }

            _uiState.update { it.copy(isLoading = true, error = null) }

            try {
                val negocioCreado = negocioRepository.createNegocio(state.negocioNombre)
                if (!negocioCreado) {
                    _uiState.update { it.copy(isLoading = false, error = "Error al crear el negocio") }
                    return@launch
                }

                val negocios = negocioRepository.getNegocios()
                val negocio = negocios.find { it.nombre_negocio == state.negocioNombre }
                if (negocio == null) {
                    _uiState.update { it.copy(isLoading = false, error = "No se pudo obtener el ID del negocio creado") }
                    return@launch
                }

                val negocioId = negocio.id

                val adminCreado = usuarioRepository.createUsuario(
                    username = state.adminUsername,
                    nombre = state.adminNombre,
                    password = "",
                    rol = "admin_almacen",
                    clienteId = negocioId,
                    almacenId = "0",
                    androidId = state.adminAndroidId
                )
                if (!adminCreado) {
                    _uiState.update { it.copy(isLoading = false, error = "Error al crear el administrador") }
                    return@launch
                }

                for (sucursal in state.sucursales) {
                    val localCreado = localRepository.createLocal(
                        clienteId = negocioId,
                        nombre = sucursal.nombre,
                        ruc = sucursal.ruc,
                        direccion = sucursal.direccion
                    )
                    if (!localCreado) {
                        _uiState.update { it.copy(isLoading = false, error = "Error al crear la sucursal ${sucursal.nombre}") }
                        return@launch
                    }
                }

                for (vendedor in state.vendedores) {
                    val vendedorCreado = usuarioRepository.createUsuario(
                        username = vendedor.usuario,
                        nombre = vendedor.nombre,
                        password = vendedor.password,
                        rol = "seller",
                        clienteId = negocioId,
                        almacenId = vendedor.localSeleccionado,
                        androidId = vendedor.androidId
                    )
                    if (!vendedorCreado) {
                        _uiState.update { it.copy(isLoading = false, error = "Error al crear el vendedor ${vendedor.nombre}") }
                        return@launch
                    }
                }

                val licenciaActivada = licenciaRepository.activateLicense(
                    clienteId = negocioId,
                    deviceId = state.adminAndroidId,
                    dias = state.diasLicencia
                )
                if (!licenciaActivada) {
                    _uiState.update { it.copy(isLoading = false, error = "Error al activar la licencia") }
                    return@launch
                }

                _uiState.update { it.copy(isLoading = false, success = true, error = null) }

            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message ?: "Error inesperado al registrar el cliente") }
            }
        }
    }

    fun limpiarError() {
        _uiState.update { it.copy(error = null) }
    }

    fun resetSuccess() {
        _uiState.update { it.copy(success = false) }
    }
}
