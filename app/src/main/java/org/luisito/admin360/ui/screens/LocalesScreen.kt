package org.luisito.admin360.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import org.luisito.admin360.ui.viewmodels.LocalViewModel
import org.luisito.admin360.ui.viewmodels.NegocioActivoViewModel
import org.luisito.admin360.ui.viewmodels.UsuarioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocalesScreen(
    onBack: () -> Unit,
    localViewModel: LocalViewModel = viewModel(),
    negocioActivoViewModel: NegocioActivoViewModel = viewModel(),
    usuarioViewModel: UsuarioViewModel = viewModel()
) {
    val uiState by localViewModel.uiState.collectAsState()
    val negocioId by negocioActivoViewModel.negocioId.collectAsState()
    val negocioIdLocal = negocioId
    val negocioNombre by negocioActivoViewModel.negocioNombre.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var deleteLocalId by remember { mutableStateOf<String?>(null) }
    var showEditDialog by remember { mutableStateOf(false) }
    var editLocalId by remember { mutableStateOf<String?>(null) }
    var editNombre by remember { mutableStateOf("") }
    var editRuc by remember { mutableStateOf("") }
    var editDireccion by remember { mutableStateOf("") }
    var editActivo by remember { mutableStateOf(true) }
    
    var showVendedorDialog by remember { mutableStateOf(false) }
    var vendedorLocalId by remember { mutableStateOf<String?>(null) }
    var vendedorNombre by remember { mutableStateOf("") }
    var vendedorUsuario by remember { mutableStateOf("") }
    var vendedorAndroidId by remember { mutableStateOf("") }
    var vendedorPassword by remember { mutableStateOf("") }

    LaunchedEffect(negocioIdLocal) {
        localViewModel.loadLocales()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("📋 Locales - ${negocioNombre ?: "Sin negocio"}") },
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
                        onClick = { localViewModel.loadLocales() }
                    ) {
                        Text("🔄 Reintentar")
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(uiState.locales) { local ->
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
                                        text = "N${negocioIdLocal}L${local.id} - ${local.nombre}",
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Text(
                                        text = "RUC: ${local.ruc} | Dirección: ${local.direccion}",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                    Text(
                                        text = "${if (local.activo) "🟢 Activo" else "🔴 Inactivo"}",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                                Column {
                                    Row {
                                        IconButton(
                                            onClick = {
                                                editLocalId = local.id
                                                editNombre = local.nombre
                                                editRuc = local.ruc
                                                editDireccion = local.direccion
                                                editActivo = local.activo
                                                showEditDialog = true
                                            }
                                        ) {
                                            Icon(Icons.Default.Edit, contentDescription = "Editar")
                                        }
                                        IconButton(
                                            onClick = {
                                                deleteLocalId = local.id
                                                showDeleteDialog = true
                                            }
                                        ) {
                                            Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                                        }
                                    }
                                    Button(
                                        onClick = {
                                            vendedorLocalId = local.id
                                            vendedorNombre = ""
                                            vendedorUsuario = ""
                                            vendedorAndroidId = ""
                                            vendedorPassword = ""
                                            showVendedorDialog = true
                                        },
                                        modifier = Modifier.fillMaxWidth(),
                                        colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                                            containerColor = Color(0xFF4CAF50)
                                        )
                                    ) {
                                        Icon(Icons.Default.Add, contentDescription = "Crear Vendedor")
                                        Text("Crear Vendedor")
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
                        onClick = { localViewModel.paginaAnterior() },
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
                        onClick = { localViewModel.siguientePagina() },
                        enabled = uiState.paginaActual < uiState.totalPaginas,
                        modifier = Modifier.padding(4.dp)
                    ) {
                        Text("▶")
                    }
                }
            }
        }
    }

    // Dialog: Crear Vendedor
    if (showVendedorDialog && vendedorLocalId != null) {
        val localId = vendedorLocalId ?: ""
        AlertDialog(
            onDismissRequest = { showVendedorDialog = false },
            title = { Text("🧑 Crear Vendedor") },
            text = {
                Column {
                    Text(
                        text = "Local: N${negocioIdLocal}L${localId}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = vendedorNombre,
                        onValueChange = { vendedorNombre = it },
                        label = { Text("Nombre completo *") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = vendedorUsuario,
                        onValueChange = { vendedorUsuario = it },
                        label = { Text("Usuario *") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = vendedorPassword,
                        onValueChange = { vendedorPassword = it },
                        label = { Text("Contraseña *") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = vendedorAndroidId,
                        onValueChange = { vendedorAndroidId = it },
                        label = { Text("Android ID *") },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("G360-XXXXXXXXXXXX") }
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (vendedorNombre.isNotEmpty() && vendedorUsuario.isNotEmpty() && vendedorPassword.isNotEmpty() && vendedorAndroidId.isNotEmpty() && negocioIdLocal != null) {
                            usuarioViewModel.createUsuario(
                                username = vendedorUsuario,
                                nombre = vendedorNombre,
                                password = vendedorPassword,
                                rol = "seller",
                                almacenId = localId,
                                androidId = vendedorAndroidId
                            )
                            showVendedorDialog = false
                            vendedorNombre = ""
                            vendedorUsuario = ""
                            vendedorPassword = ""
                            vendedorAndroidId = ""
                            vendedorLocalId = null
                        }
                    }
                ) {
                    Text("Crear Vendedor")
                }
            },
            dismissButton = {
                TextButton(onClick = { showVendedorDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    // Dialog: Eliminar
    if (showDeleteDialog && deleteLocalId != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("⚠️ Eliminar local") },
            text = { Text("¿Estás seguro?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        deleteLocalId?.let { 
                            localViewModel.deleteLocal(it)
                            showDeleteDialog = false
                            deleteLocalId = null
                        }
                    }
                ) {
                    Text("Eliminar", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false; deleteLocalId = null }) {
                    Text("Cancelar")
                }
            }
        )
    }

    // Dialog: Editar
    if (showEditDialog && editLocalId != null) {
        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            title = { Text("✏️ Editar local") },
            text = {
                Column {
                    OutlinedTextField(
                        value = editNombre,
                        onValueChange = { editNombre = it },
                        label = { Text("Nombre") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = editRuc,
                        onValueChange = { editRuc = it },
                        label = { Text("RUC") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = editDireccion,
                        onValueChange = { editDireccion = it },
                        label = { Text("Dirección") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        editLocalId?.let {
                            localViewModel.updateLocal(
                                it, 
                                editNombre, 
                                editRuc, 
                                editDireccion, 
                                editActivo
                            )
                            showEditDialog = false
                            editLocalId = null
                        }
                    }
                ) {
                    Text("Guardar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditDialog = false; editLocalId = null }) {
                    Text("Cancelar")
                }
            }
        )
    }

    // Dialog: Crear local
    if (showDialog) {
        var newNombre by remember { mutableStateOf("") }
        var newRuc by remember { mutableStateOf("") }
        var newDireccion by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("🏢 Nuevo local") },
            text = {
                Column {
                    OutlinedTextField(
                        value = newNombre,
                        onValueChange = { newNombre = it },
                        label = { Text("Nombre del local") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = newRuc,
                        onValueChange = { newRuc = it },
                        label = { Text("RUC") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = newDireccion,
                        onValueChange = { newDireccion = it },
                        label = { Text("Dirección") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (newNombre.isNotEmpty() && newRuc.isNotEmpty() && newDireccion.isNotEmpty() && negocioIdLocal != null) {
                            localViewModel.createLocal(newNombre, newRuc, newDireccion)
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
