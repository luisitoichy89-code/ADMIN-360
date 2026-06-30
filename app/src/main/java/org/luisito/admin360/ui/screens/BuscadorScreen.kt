package org.luisito.admin360.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.luisito.admin360.ui.viewmodels.BuscadorResultado
import org.luisito.admin360.ui.viewmodels.BuscadorViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuscadorScreen(
    onBack: () -> Unit,
    viewModel: BuscadorViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var busqueda by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("🔍 Buscador General") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Text("←", color = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = busqueda,
                    onValueChange = { busqueda = it.uppercase() },
                    label = { Text("Buscar por código (ej: N5, N5L2, N5A1)") },
                    modifier = Modifier.weight(1f),
                    isError = uiState.error != null
                )
                Button(
                    onClick = {
                        if (busqueda.isNotEmpty()) {
                            viewModel.buscar(busqueda)
                        }
                    }
                ) {
                    Icon(Icons.Default.Search, contentDescription = "Buscar")
                }
            }

            if (uiState.error != null) {
                Text(
                    text = "❌ ${uiState.error}",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 8.dp)
                )
                Button(
                    onClick = { viewModel.buscar(busqueda) },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text("🔄 Reintentar")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                return@Column
            }

            when (val resultado = uiState.resultado) {
                is BuscadorResultado.NegocioEncontrado -> {
                    ResultadoNegocio(
                        negocio = resultado.negocio,
                        onPausar = {
                            dialogMessage = "¿Pausar negocio ${resultado.negocio.nombre_negocio}?"
                            showDialog = true
                        },
                        onReanudar = {
                            dialogMessage = "¿Reanudar negocio ${resultado.negocio.nombre_negocio}?"
                            showDialog = true
                        }
                    )
                }
                is BuscadorResultado.LocalEncontrado -> {
                    ResultadoLocal(
                        local = resultado.local,
                        negocioNombre = resultado.negocioNombre
                    )
                }
                is BuscadorResultado.UsuarioEncontrado -> {
                    ResultadoUsuario(
                        usuario = resultado.usuario,
                        negocioNombre = resultado.negocioNombre
                    )
                }
                is BuscadorResultado.LicenciaEncontrada -> {
                    ResultadoLicencia(
                        licencia = resultado.licencia,
                        negocioNombre = resultado.negocioNombre
                    )
                }
                is BuscadorResultado.NoEncontrado -> {
                    Text(
                        text = "❌ No se encontró el código",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
                null -> {
                    Text(
                        text = "Ingresa un código para buscar",
                        color = Color.Gray,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Confirmar") },
            text = { Text(dialogMessage) },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDialog = false
                    }
                ) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
fun ResultadoNegocio(
    negocio: org.luisito.admin360.data.models.Negocio,
    onPausar: () -> Unit,
    onReanudar: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "🏢 N${negocio.id} - ${negocio.nombre_negocio}",
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = "Estado: ${if (negocio.activo) "🟢 Activo" else "🔴 Pausado"}",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = { /* TODO: Crear admin */ }
                ) {
                    Text("👤 Crear Admin")
                }
                Button(
                    onClick = { /* TODO: Crear local */ }
                ) {
                    Text("📋 Crear Local")
                }
                Button(
                    onClick = { /* TODO: Activar licencia */ }
                ) {
                    Text("🔑 Licencia")
                }
                if (negocio.activo) {
                    Button(
                        onClick = onPausar,
                        colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFF5722)
                        )
                    ) {
                        Icon(Icons.Default.PlayArrow, contentDescription = "Pausar")
                        Text("Pausar")
                    }
                } else {
                    Button(
                        onClick = onReanudar,
                        colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4CAF50)
                        )
                    ) {
                        Icon(Icons.Default.PlayArrow, contentDescription = "Reanudar")
                        Text("Reanudar")
                    }
                }
            }
        }
    }
}

@Composable
fun ResultadoLocal(
    local: org.luisito.admin360.data.models.Local,
    negocioNombre: String
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "📍 N${local.cliente_id}L${local.id} - ${local.nombre}",
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = "Negocio: $negocioNombre",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "RUC: ${local.ruc} | Dirección: ${local.direccion}",
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { /* TODO: Crear vendedor */ }
                ) {
                    Text("🧑 Crear Vendedor")
                }
                Button(
                    onClick = { /* TODO: Eliminar local */ },
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFC62828)
                    )
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                    Text("Eliminar")
                }
            }
        }
    }
}

@Composable
fun ResultadoUsuario(
    usuario: org.luisito.admin360.data.models.User,
    negocioNombre: String
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            val codigo = if (usuario.rol == "admin_almacen") {
                "N${usuario.cliente_id}A${usuario.id}"
            } else {
                "N${usuario.cliente_id}L${usuario.almacen_id}V${usuario.id}"
            }
            Text(
                text = "👤 $codigo - ${usuario.nombre ?: usuario.username}",
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = "Negocio: $negocioNombre",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Rol: ${usuario.rol} | ${if (usuario.activo) "🟢 Activo" else "🔴 Inactivo"}",
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { /* TODO: Resetear password */ }
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = "Resetear")
                    Text("Resetear Pass")
                }
                Button(
                    onClick = { /* TODO: Eliminar usuario */ },
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFC62828)
                    )
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                    Text("Eliminar")
                }
            }
        }
    }
}

@Composable
fun ResultadoLicencia(
    licencia: org.luisito.admin360.data.models.Licencia,
    negocioNombre: String
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "🔑 N${licencia.cliente_id}LIC${licencia.id}",
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = "Negocio: $negocioNombre",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Device: ${licencia.device_id}",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "Expira: ${licencia.expiracion ?: "N/A"} | ${licencia.getEstado()}",
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { /* TODO: Renovar licencia */ }
                ) {
                    Text("🔄 Renovar")
                }
                Button(
                    onClick = { /* TODO: Eliminar licencia */ },
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFC62828)
                    )
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                    Text("Eliminar")
                }
            }
        }
    }
}
