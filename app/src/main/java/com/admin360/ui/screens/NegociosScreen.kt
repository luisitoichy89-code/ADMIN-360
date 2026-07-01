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
import com.admin360.feature.negocios.model.NegocioDto
import com.admin360.feature.negocios.viewmodel.NegociosViewModel
import com.admin360.ui.components.ConfirmDialog
import com.admin360.ui.components.ErrorBanner
import com.admin360.ui.components.FormDialog

@Composable
fun NegociosScreen(
    onBack: () -> Unit,
    viewModel: NegociosViewModel = viewModel()
) {
    val form = remember { FormController() }
    val state = viewModel.state
    var showForm by remember { mutableStateOf(false) }
    var deleteId by remember { mutableStateOf<String?>(null) }
    var nombre by remember { mutableStateOf("") }
    var propietario by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Negocios") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Text("←")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showForm = true }) {
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
                onRetry = { viewModel.load() }
            )

            if (state.loading) {
                CircularProgressIndicator()
            }

            LazyColumn {
                items(state.negocios) { negocio ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(negocio.nombre, style = MaterialTheme.typography.titleLarge)
                            Text("Propietario: ${negocio.propietario}")
                            Text("Email: ${negocio.email ?: "-"}")
                            Row {
                                Button(
                                    onClick = {
                                        deleteId = negocio.id
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
        title = "Crear Negocio",
        show = showForm,
        loading = form.state.loading,
        error = form.state.error,
        onDismiss = {
            showForm = false
            nombre = ""
            propietario = ""
            form.reset()
        },
        onConfirm = {
            if (!Validator.required(nombre)) return@FormDialog
            if (!Validator.required(propietario)) return@FormDialog

            form.submit(
                action = {
                    viewModel.create(
                        NegocioDto(
                            nombre = nombre,
                            propietario = propietario
                        )
                    )
                },
                onSuccess = {
                    showForm = false
                    nombre = ""
                    propietario = ""
                    viewModel.load()
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
                value = propietario,
                onValueChange = { propietario = it },
                label = { Text("Propietario") },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

    ConfirmDialog(
        show = deleteId != null,
        title = "Eliminar Negocio",
        message = "¿Estás seguro de eliminar este negocio?",
        onConfirm = {
            deleteId?.let { viewModel.delete(it) }
            deleteId = null
            viewModel.load()
        },
        onDismiss = { deleteId = null }
    )
}
