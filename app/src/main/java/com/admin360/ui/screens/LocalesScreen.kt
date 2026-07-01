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
import com.admin360.feature.locales.model.LocalDto
import com.admin360.feature.locales.viewmodel.LocalesViewModel
import com.admin360.ui.components.ConfirmDialog
import com.admin360.ui.components.ErrorBanner
import com.admin360.ui.components.FormDialog

@Composable
fun LocalesScreen(
    onBack: () -> Unit,
    negocioId: String? = null,
    viewModel: LocalesViewModel = viewModel()
) {
    val form = remember { FormController() }
    val state = viewModel.state
    var showForm by remember { mutableStateOf(false) }
    var deleteId by remember { mutableStateOf<String?>(null) }
    var nombre by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }

    LaunchedEffect(negocioId) {
        if (negocioId != null) {
            viewModel.load(negocioId)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Locales") },
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
                items(state.locales) { local ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(local.nombre, style = MaterialTheme.typography.titleLarge)
                            Text("Dirección: ${local.direccion ?: "-"}")
                            Row {
                                Button(
                                    onClick = {
                                        deleteId = local.id
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
        title = "Crear Local",
        show = showForm,
        loading = form.state.loading,
        error = form.state.error,
        onDismiss = {
            showForm = false
            nombre = ""
            direccion = ""
            form.reset()
        },
        onConfirm = {
            if (!Validator.required(nombre)) return@FormDialog

            form.submit(
                action = {
                    viewModel.create(
                        LocalDto(
                            negocio_id = negocioId ?: "",
                            nombre = nombre,
                            direccion = direccion
                        )
                    )
                },
                onSuccess = {
                    showForm = false
                    nombre = ""
                    direccion = ""
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
                value = direccion,
                onValueChange = { direccion = it },
                label = { Text("Dirección") },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

    ConfirmDialog(
        show = deleteId != null,
        title = "Eliminar Local",
        message = "¿Estás seguro de eliminar este local?",
        onConfirm = {
            deleteId?.let { viewModel.delete(it, negocioId ?: "") }
            deleteId = null
            negocioId?.let { viewModel.load(it) }
        },
        onDismiss = { deleteId = null }
    )
}
