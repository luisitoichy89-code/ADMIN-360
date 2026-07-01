package com.admin360.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.admin360.core.form.FormController
import com.admin360.core.validation.Validator
import com.admin360.feature.usuarios.model.UsuarioDto
import com.admin360.feature.usuarios.viewmodel.UsuariosViewModel
import com.admin360.ui.components.ConfirmDialog
import com.admin360.ui.components.ErrorBanner
import com.admin360.ui.components.FormDialog

@Composable
fun UsuariosScreen(
    onBack: () -> Unit,
    negocioId: String? = null,
    viewModel: UsuariosViewModel = viewModel()
) {
    val form = remember { FormController() }
    val state = viewModel.state
    var showForm by remember { mutableStateOf(false) }
    var deleteId by remember { mutableStateOf<String?>(null) }
    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var rol by remember { mutableStateOf("VENDEDOR") }

    LaunchedEffect(negocioId) {
        if (negocioId != null) {
            viewModel.load(negocioId)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Usuarios") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Text("←")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { 
                    if (negocioId != null) showForm = true 
                },
                enabled = negocioId != null
            ) {
                Text("+")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            ErrorBanner(
                error = state.error,
                onRetry = { negocioId?.let { viewModel.load(it) } }
            )

            if (negocioId == null) {
                Text("Selecciona un negocio primero")
                return@Column
            }

            if (state.loading) {
                CircularProgressIndicator()
            }

            LazyColumn {
                items(state.usuarios) { user ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(user.nombre, style = MaterialTheme.typography.titleLarge)
                            Text("Email: ${user.email}")
                            Text("Rol: ${user.rol}")
                            Row {
                                Button(
                                    onClick = {
                                        deleteId = user.id
                                    }
                                ) {
                                    Text("Eliminar")
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    FormDialog(
        title = "Crear Usuario",
        show = showForm,
        loading = form.state.loading,
        error = form.state.error,
        onDismiss = {
            showForm = false
            nombre = ""
            email = ""
            rol = "VENDEDOR"
            form.reset()
        },
        onConfirm = {
            if (!Validator.required(nombre)) return@FormDialog
            if (!Validator.email(email)) return@FormDialog

            form.submit(
                action = {
                    viewModel.create(
                        UsuarioDto(
                            negocio_id = negocioId ?: "",
                            nombre = nombre,
                            email = email,
                            rol = rol
                        ),
                        negocioId ?: ""
                    )
                },
                onSuccess = {
                    showForm = false
                    nombre = ""
                    email = ""
                    rol = "VENDEDOR"
                    negocioId?.let { viewModel.load(it) }
                }
            )
        }
    ) {
        Column {
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = rol,
                onValueChange = { rol = it },
                label = { Text("Rol (ADMIN / VENDEDOR)") },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

    ConfirmDialog(
        show = deleteId != null,
        title = "Eliminar Usuario",
        message = "¿Estás seguro de eliminar este usuario?",
        onConfirm = {
            deleteId?.let { viewModel.delete(it, negocioId ?: "") }
            deleteId = null
            negocioId?.let { viewModel.load(it) }
        },
        onDismiss = { deleteId = null }
    )
}
