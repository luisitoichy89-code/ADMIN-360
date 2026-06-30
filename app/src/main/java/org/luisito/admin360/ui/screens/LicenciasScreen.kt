package org.luisito.admin360.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.runtime.LaunchedEffect
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
import org.luisito.admin360.ui.viewmodels.LicenciaViewModel
import org.luisito.admin360.ui.viewmodels.NegocioActivoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LicenciasScreen(
    onBack: () -> Unit,
    licenciaViewModel: LicenciaViewModel = viewModel(),
    negocioActivoViewModel: NegocioActivoViewModel = viewModel()
) {
    val uiState by licenciaViewModel.uiState.collectAsState()
    val negocioId by negocioActivoViewModel.negocioId.collectAsState()
    val negocioIdLocal = negocioId
    val negocioNombre by negocioActivoViewModel.negocioNombre.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var deleteLicenciaId by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(negocioIdLocal) {
        licenciaViewModel.loadLicencias()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("📜 Licencias - ${negocioNombre ?: "Sin negocio"}") },
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
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { 
                if (negocioIdLocal != null) showDialog = true 
            }) {
                Icon(Icons.Default.Add, contentDescription = "Agregar")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(20.dp)
        ) {
            if (negocioIdLocal == null) {
                Text(
                    text = "⚠️ Selecciona un negocio primero",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            } else if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else if (uiState.error != null) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "❌ ${uiState.error}",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    Button(
                        onClick = { licenciaViewModel.loadLicencias() }
                    ) {
                        Text("🔄 Reintentar")
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(uiState.licencias) { licencia ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "N${negocioIdLocal}LIC${licencia.id} - Device: ${licencia.device_id}",
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Text(
                                        text = "Expira: ${licencia.expiracion ?: "N/A"} | ${licencia.getEstado()}",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                    Text(
                                        text = "${if (licencia.activo) "🟢 Activa" else "🔴 Inactiva"}",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                                Row {
                                    IconButton(
                                        onClick = {
                                            deleteLicenciaId = licencia.id
                                            showDeleteDialog = true
                                        }
                                    ) {
                                        Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                                    }
                                }
                            }
                        }
                    }
                }
                
                // Paginación
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = { licenciaViewModel.paginaAnterior() },
                        enabled = uiState.paginaActual > 1,
                        modifier = Modifier.padding(4.dp)
                    ) {
                        Text("◀")
                    }
                    Text(
                        text = "${uiState.paginaActual} / ${uiState.totalPaginas}",
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .align(Alignment.CenterVertically)
                    )
                    Button(
                        onClick = { licenciaViewModel.siguientePagina() },
                        enabled = uiState.paginaActual < uiState.totalPaginas,
                        modifier = Modifier.padding(4.dp)
                    ) {
                        Text("▶")
                    }
                }
            }
        }
    }

    // Dialog: Eliminar
    if (showDeleteDialog && deleteLicenciaId != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("⚠️ Eliminar licencia") },
            text = { Text("¿Estás seguro?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        deleteLicenciaId?.let { 
                            licenciaViewModel.deleteLicense(it)
                            showDeleteDialog = false
                            deleteLicenciaId = null
                        }
                    }
                ) {
                    Text("Eliminar", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false; deleteLicenciaId = null }) {
                    Text("Cancelar")
                }
            }
        )
    }

    // Dialog: Crear licencia
    if (showDialog) {
        var newDeviceId by remember { mutableStateOf("") }
        var newDias by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("📜 Nueva licencia") },
            text = {
                Column {
                    OutlinedTextField(
                        value = newDeviceId,
                        onValueChange = { newDeviceId = it },
                        label = { Text("Device ID") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = newDias,
                        onValueChange = { newDias = it },
                        label = { Text("Días de validez") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (newDeviceId.isNotEmpty() && newDias.isNotEmpty() && negocioIdLocal != null) {
                            licenciaViewModel.activateLicense(newDeviceId, newDias.toIntOrNull() ?: 30)
                            showDialog = false
                        }
                    }
                ) {
                    Text("Activar")
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
