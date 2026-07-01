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
import com.admin360.feature.licencias.model.LicenciaDto
import com.admin360.feature.licencias.viewmodel.LicenciasViewModel
import com.admin360.ui.components.ConfirmDialog
import com.admin360.ui.components.ErrorBanner
import com.admin360.ui.components.FormDialog

@Composable
fun LicenciasScreen(
    onBack: () -> Unit,
    negocioId: String? = null,
    viewModel: LicenciasViewModel = viewModel()
) {
    val form = remember { FormController() }
    val state = viewModel.state
    var showForm by remember { mutableStateOf(false) }
    var deleteId by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(negocioId) {
        if (negocioId != null) {
            viewModel.load(negocioId)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Licencias") },
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
                items(state.licencias) { licencia ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("Licencia", style = MaterialTheme.typography.titleLarge)
                            Text("Estado: ${licencia.estado}")
                            Text("Android ID: ${licencia.android_id ?: "-"}")
                            Row {
                                Button(
                                    onClick = {
                                        deleteId = licencia.id
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
        title = "Crear Licencia",
        show = showForm,
        loading = form.state.loading,
        error = form.state.error,
        onDismiss = {
            showForm = false
            form.reset()
        },
        onConfirm = {
            form.submit(
                action = {
                    viewModel.create(
                        LicenciaDto(
                            negocio_id = negocioId ?: "",
                            estado = "ACTIVA"
                        ),
                        negocioId ?: ""
                    )
                },
                onSuccess = {
                    showForm = false
                    negocioId?.let { viewModel.load(it) }
                }
            )
        }
    ) {
        Column {
            Text("La licencia se creará con estado ACTIVA")
        }
    }

    ConfirmDialog(
        show = deleteId != null,
        title = "Eliminar Licencia",
        message = "¿Estás seguro de eliminar esta licencia?",
        onConfirm = {
            deleteId?.let { viewModel.delete(it, negocioId ?: "") }
            deleteId = null
            negocioId?.let { viewModel.load(it) }
        },
        onDismiss = { deleteId = null }
    )
}
