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
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import org.luisito.admin360.data.models.Local
import org.luisito.admin360.ui.viewmodels.NegocioActivoViewModel
import org.luisito.admin360.ui.viewmodels.UsuarioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminUsersScreen(
    onBack: () -> Unit,
    locales: List<Local> = emptyList(),
    usuarioViewModel: UsuarioViewModel = viewModel(),
    negocioActivoViewModel: NegocioActivoViewModel = viewModel()
) {
    val uiState by usuarioViewModel.uiState.collectAsState()
    val negocioId by negocioActivoViewModel.negocioId.collectAsState()
    val negocioIdLocal = negocioId
    val negocioNombre by negocioActivoViewModel.negocioNombre.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var deleteUserId by remember { mutableStateOf<String?>(null) }
    var showEditDialog by remember { mutableStateOf(false) }
    var editUserId by remember { mutableStateOf<String?>(null) }
    var showResetDialog by remember { mutableStateOf(false) }
    var resetUserId by remember { mutableStateOf<String?>(null) }

    var newUsername by remember { mutableStateOf("") }
    var newNombre by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var newRol by remember { mutableStateOf("seller") }
    var newAlmacenId by remember { mutableStateOf("") }
    var newAndroidId by remember { mutableStateOf("") }

    var editUsername by remember { mutableStateOf("") }
    var editNombre by remember { mutableStateOf("") }
    var editRol by remember { mutableStateOf("seller") }
    var editAlmacenId by remember { mutableStateOf("") }
    var editActivo by remember { mutableStateOf(true) }

    var showRolMenu by remember { mutableStateOf(false) }
    var showEditRolMenu by remember { mutableStateOf(false) }

    val roles = listOf("admin_almacen", "seller")

    LaunchedEffect(negocioIdLocal) {
        usuarioViewModel.loadUsuarios()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("👥 Usuarios - ${negocioNombre ?: "Sin negocio"}") },
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
                        onClick = { usuarioViewModel.loadUsuarios() }
                    ) {
                        Text("🔄 Reintentar")
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(uiState.usuarios) { usuario ->
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
                                    val codigo = if (usuario.rol == "admin_almacen") {
                                        "N${negocioIdLocal}A${usuario.id}"
                                    } else {
                                        "N${negocioIdLocal}L${usuario.almacen_id}V${usuario.id}"
                                    }
                                    Text(
                                        text = "$codigo - ${usuario.nombre ?: usuario.username}",
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Text(
                                        text = "Usuario: ${usuario.username} | Rol: ${usuario.rol} | ${if (usuario.activo) "🟢 Activo" else "🔴 Inactivo"}",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                    Text(
                                        text = "Local: ${usuario.almacen_id}",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                                Row {
                                    IconButton(
                                        onClick = {
                                            resetUserId = usuario.id
                                            showResetDialog = true
                                        }
                                    ) {
                                        Icon(Icons.Default.Refresh, contentDescription = "Resetear password")
                                    }
                                    IconButton(
                                        onClick = {
                                            editUserId = usuario.id
                                            editUsername = usuario.username
                                            editNombre = usuario.nombre ?: ""
                                            editRol = usuario.rol
                                            editAlmacenId = usuario.almacen_id
                                            editActivo = usuario.activo
                                            showEditDialog = true
                                        }
                                    ) {
                                        Icon(Icons.Default.Edit, contentDescription = "Editar")
                                    }
                                    IconButton(
                                        onClick = {
                                            deleteUserId = usuario.id
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
                        onClick = { usuarioViewModel.paginaAnterior() },
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
                        onClick = { usuarioViewModel.siguientePagina() },
                        enabled = uiState.paginaActual < uiState.totalPaginas,
                        modifier = Modifier.padding(4.dp)
                    ) {
                        Text("▶")
                    }
                }
            }
        }
    }

    // Dialog: Crear usuario
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("👤 Nuevo usuario") },
            text = {
                Column {
                    OutlinedTextField(
                        value = newUsername,
                        onValueChange = { newUsername = it },
                        label = { Text("Usuario") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = newNombre,
                        onValueChange = { newNombre = it },
                        label = { Text("Nombre completo") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = newPassword,
                        onValueChange = { newPassword = it },
                        label = { Text("Contraseña") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = newAndroidId,
                        onValueChange = { newAndroidId = it },
                        label = { Text("Android ID") },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("G360-XXXXXXXXXXXX") },
                        isError = uiState.androidIdDuplicado
                    )
                    if (uiState.androidIdDuplicado) {
                        Text(
                            text = "⚠️ Este Android ID ya está registrado",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    OutlinedTextField(
                        value = newAlmacenId,
                        onValueChange = { newAlmacenId = it },
                        label = { Text("ID del local") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Column {
                        Text("Rol: $newRol", style = MaterialTheme.typography.bodyMedium)
                        TextButton(onClick = { showRolMenu = true }) {
                            Text("Seleccionar rol")
                        }
                        DropdownMenu(
                            expanded = showRolMenu,
                            onDismissRequest = { showRolMenu = false }
                        ) {
                            roles.forEach { rol ->
                                DropdownMenuItem(
                                    text = { Text(rol) },
                                    onClick = {
                                        newRol = rol
                                        showRolMenu = false
                                    }
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (newUsername.isNotEmpty() && newPassword.isNotEmpty() && newAlmacenId.isNotEmpty() && newAndroidId.isNotEmpty() && negocioIdLocal != null) {
                            usuarioViewModel.createUsuario(
                                username = newUsername,
                                nombre = newNombre,
                                password = newPassword,
                                rol = newRol,
                                almacenId = newAlmacenId,
                                androidId = newAndroidId
                            )
                            showDialog = false
                            newUsername = ""
                            newNombre = ""
                            newPassword = ""
                            newAlmacenId = ""
                            newRol = "seller"
                            newAndroidId = ""
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

    // Dialog: Eliminar usuario
    if (showDeleteDialog && deleteUserId != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("⚠️ Eliminar usuario") },
            text = { Text("¿Estás seguro de eliminar este usuario?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        deleteUserId?.let { 
                            usuarioViewModel.deleteUsuario(it)
                            showDeleteDialog = false
                            deleteUserId = null
                        }
                    }
                ) {
                    Text("Eliminar", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false; deleteUserId = null }) {
                    Text("Cancelar")
                }
            }
        )
    }

    // Dialog: Resetear contraseña
    if (showResetDialog && resetUserId != null) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text("🔑 Resetear contraseña") },
            text = { Text("¿Restablecer contraseña a '123456'?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        resetUserId?.let { 
                            usuarioViewModel.resetPassword(it)
                            showResetDialog = false
                            resetUserId = null
                        }
                    }
                ) {
                    Text("Resetear", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false; resetUserId = null }) {
                    Text("Cancelar")
                }
            }
        )
    }

    // Dialog: Editar usuario
    if (showEditDialog && editUserId != null) {
        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            title = { Text("✏️ Editar usuario") },
            text = {
                Column {
                    OutlinedTextField(
                        value = editUsername,
                        onValueChange = { editUsername = it },
                        label = { Text("Usuario") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = editNombre,
                        onValueChange = { editNombre = it },
                        label = { Text("Nombre completo") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = editAlmacenId,
                        onValueChange = { editAlmacenId = it },
                        label = { Text("ID del local") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Column {
                        Text("Rol: $editRol", style = MaterialTheme.typography.bodyMedium)
                        TextButton(onClick = { showEditRolMenu = true }) {
                            Text("Seleccionar rol")
                        }
                        DropdownMenu(
                            expanded = showEditRolMenu,
                            onDismissRequest = { showEditRolMenu = false }
                        ) {
                            roles.forEach { rol ->
                                DropdownMenuItem(
                                    text = { Text(rol) },
                                    onClick = {
                                        editRol = rol
                                        showEditRolMenu = false
                                    }
                                )
                            }
                        }
                    }
                    TextButton(
                        onClick = { editActivo = !editActivo },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(if (editActivo) "🟢 Activo" else "🔴 Inactivo")
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        editUserId?.let {
                            usuarioViewModel.updateUsuario(
                                id = it,
                                username = editUsername,
                                nombre = editNombre,
                                rol = editRol,
                                almacenId = editAlmacenId,
                                activo = editActivo
                            )
                            showEditDialog = false
                            editUserId = null
                        }
                    }
                ) {
                    Text("Guardar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditDialog = false; editUserId = null }) {
                    Text("Cancelar")
                }
            }
        )
    }
}
