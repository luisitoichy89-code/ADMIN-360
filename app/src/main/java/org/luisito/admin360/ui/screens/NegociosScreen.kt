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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
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
import org.luisito.admin360.data.SessionManager
import org.luisito.admin360.ui.viewmodels.NegocioActivoViewModel
import org.luisito.admin360.ui.viewmodels.NegocioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NegociosScreen(
    onBack: () -> Unit,
    onNegocioSeleccionado: (String) -> Unit,
    negocioViewModel: NegocioViewModel = viewModel(),
    negocioActivoViewModel: NegocioActivoViewModel = viewModel()
) {
    val uiState by negocioViewModel.uiState.collectAsState()
    val negocioActivoId by negocioActivoViewModel.negocioActivoCheck.collectAsState()
    
    var showDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var deleteNegocioId by remember { mutableStateOf<String?>(null) }
    var showEditDialog by remember { mutableStateOf(false) }
    var editNegocioId by remember { mutableStateOf<String?>(null) }
    var editNombre by remember { mutableStateOf("") }
    var editActivo by remember { mutableStateOf(true) }

    val sessionManager = SessionManager

    LaunchedEffect(Unit) {
        negocioViewModel.loadNegocios()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("🏢 Negocios") },
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
            FloatingActionButton(onClick = { showDialog = true }) {
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
            if (uiState.isLoading) {
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
                        onClick = { negocioViewModel.loadNegocios() }
                    ) {
                        Text("🔄 Reintentar")
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(uiState.negocios) { negocio ->
                        val isActivo = negocioActivoId == negocio.id
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isActivo) 
                                    MaterialTheme.colorScheme.primaryContainer 
                                else 
                                    MaterialTheme.colorScheme.surface
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    IconButton(
                                        onClick = {
                                            negocio.id?.let {
                                                // Guardar en SessionManager
                                                sessionManager.setClienteId(it)
                                                sessionManager.setNegocioId(it)
                                                negocioActivoViewModel.seleccionarNegocio(it, negocio.nombre_negocio)
                                                onNegocioSeleccionado(it)
                                            }
                                        }
                                    ) {
                                        if (isActivo) {
                                            Icon(
                                                Icons.Default.Check,
                                                contentDescription = "Seleccionado",
                                                tint = Color(0xFF4CAF50)
                                            )
                                        } else {
                                            Text("⬜", fontSize = MaterialTheme.typography.titleLarge.fontSize)
                                        }
                                    }
                                    Column {
                                        Text(
                                            text = "N${negocio.id} - ${negocio.nombre_negocio}",
                                            style = MaterialTheme.typography.titleMedium
                                        )
                                        Text(
                                            text = "${if (negocio.activo) "🟢 Activo" else "🔴 Inactivo"}",
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    }
                                }
                                Row {
                                    IconButton(
                                        onClick = {
                                            editNegocioId = negocio.id
                                            editNombre = negocio.nombre_negocio
                                            editActivo = negocio.activo
                                            showEditDialog = true
                                        }
                                    ) {
                                        Icon(Icons.Default.Edit, contentDescription = "Editar")
                                    }
                                    IconButton(
                                        onClick = {
                                            deleteNegocioId = negocio.id
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
                        onClick = { negocioViewModel.paginaAnterior() },
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
                        onClick = { negocioViewModel.siguientePagina() },
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
    if (showDeleteDialog && deleteNegocioId != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("⚠️ Eliminar negocio") },
            text = { Text("¿Estás seguro de que quieres eliminar este negocio?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        deleteNegocioId?.let { 
                            negocioViewModel.deleteNegocio(it)
                            showDeleteDialog = false
                            deleteNegocioId = null
                        }
                    }
                ) {
                    Text("Eliminar", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false; deleteNegocioId = null }) {
                    Text("Cancelar")
                }
            }
        )
    }

    // Dialog: Editar
    if (showEditDialog && editNegocioId != null) {
        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            title = { Text("✏️ Editar negocio") },
            text = {
                Column {
                    OutlinedTextField(
                        value = editNombre,
                        onValueChange = { editNombre = it },
                        label = { Text("Nombre") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        editNegocioId?.let { 
                            negocioViewModel.updateNegocio(it, editNombre, editActivo)
                            showEditDialog = false
                            editNegocioId = null
                        }
                    }
                ) {
                    Text("Guardar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditDialog = false; editNegocioId = null }) {
                    Text("Cancelar")
                }
            }
        )
    }

    // Dialog: Crear
    if (showDialog) {
        var newNombre by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("🏢 Nuevo negocio") },
            text = {
                OutlinedTextField(
                    value = newNombre,
                    onValueChange = { newNombre = it },
                    label = { Text("Nombre del negocio") },
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (newNombre.isNotEmpty()) {
                            negocioViewModel.createNegocio(newNombre)
                            showDialog = false
                        }
                    }
                ) {
                    Text("Crear")
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
