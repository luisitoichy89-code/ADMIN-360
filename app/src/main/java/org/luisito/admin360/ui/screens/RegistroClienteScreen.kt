package org.luisito.admin360.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.luisito.admin360.ui.viewmodels.RegistroClienteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroClienteScreen(
    onBack: () -> Unit,
    viewModel: RegistroClienteViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    LaunchedEffect(uiState.success) {
        if (uiState.success) {
            onBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("🏢 Registro de Cliente") },
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
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            Text(
                text = "Creación de Cliente",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Completa los datos para registrar un nuevo negocio",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // 1. Datos del Negocio
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "1. Datos del Negocio",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = uiState.negocioNombre,
                        onValueChange = viewModel::updateNegocioNombre,
                        label = { Text("Nombre del negocio *") },
                        modifier = Modifier.fillMaxWidth(),
                        isError = uiState.error != null && uiState.negocioNombre.isBlank()
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 2. Administrador Principal
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "2. Administrador Principal",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Este usuario será el jefe del negocio (admin_almacen)",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = uiState.adminNombre,
                        onValueChange = viewModel::updateAdminNombre,
                        label = { Text("Nombre completo *") },
                        modifier = Modifier.fillMaxWidth(),
                        isError = uiState.error != null && uiState.adminNombre.isBlank()
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = uiState.adminUsername,
                        onValueChange = viewModel::updateAdminUsername,
                        label = { Text("Usuario *") },
                        modifier = Modifier.fillMaxWidth(),
                        isError = uiState.error != null && uiState.adminUsername.isBlank()
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = uiState.adminAndroidId,
                        onValueChange = viewModel::updateAdminAndroidId,
                        label = { Text("Android ID *") },
                        modifier = Modifier.fillMaxWidth(),
                        isError = uiState.error != null && uiState.adminAndroidId.isBlank(),
                        placeholder = { Text("G360-XXXXXXXXXXXX") }
                    )
                    Text(
                        text = "📌 Este ID se usará para la licencia del negocio",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 3. Sucursales
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "3. Sucursales",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("Cantidad de sucursales:")
                        IconButton(
                            onClick = { 
                                viewModel.updateCantidadSucursales(uiState.cantidadSucursales - 1)
                            },
                            enabled = uiState.cantidadSucursales > 1
                        ) {
                            Text("-", fontSize = MaterialTheme.typography.titleLarge.fontSize)
                        }
                        Text(
                            text = "${uiState.cantidadSucursales}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        IconButton(
                            onClick = { 
                                viewModel.updateCantidadSucursales(uiState.cantidadSucursales + 1)
                            }
                        ) {
                            Text("+", fontSize = MaterialTheme.typography.titleLarge.fontSize)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    uiState.sucursales.forEachIndexed { index, sucursal ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp)
                            ) {
                                Text(
                                    text = "Sucursal #${index + 1}",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(4.dp))

                                OutlinedTextField(
                                    value = sucursal.nombre,
                                    onValueChange = { nuevoNombre ->
                                        viewModel.updateSucursal(index, nuevoNombre, sucursal.ruc, sucursal.direccion)
                                    },
                                    label = { Text("Nombre *") },
                                    modifier = Modifier.fillMaxWidth(),
                                    isError = uiState.error != null && sucursal.nombre.isBlank()
                                )
                                Spacer(modifier = Modifier.height(4.dp))

                                OutlinedTextField(
                                    value = sucursal.ruc,
                                    onValueChange = { nuevoRuc ->
                                        viewModel.updateSucursal(index, sucursal.nombre, nuevoRuc, sucursal.direccion)
                                    },
                                    label = { Text("RUC *") },
                                    modifier = Modifier.fillMaxWidth(),
                                    isError = uiState.error != null && sucursal.ruc.isBlank()
                                )
                                Spacer(modifier = Modifier.height(4.dp))

                                OutlinedTextField(
                                    value = sucursal.direccion,
                                    onValueChange = { nuevaDireccion ->
                                        viewModel.updateSucursal(index, sucursal.nombre, sucursal.ruc, nuevaDireccion)
                                    },
                                    label = { Text("Dirección *") },
                                    modifier = Modifier.fillMaxWidth(),
                                    isError = uiState.error != null && sucursal.direccion.isBlank()
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 4. Licencia
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "4. Licencia",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Se activará automáticamente con el Android ID del Administrador Principal",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("Días de validez:")
                        IconButton(
                            onClick = { 
                                viewModel.updateDiasLicencia(uiState.diasLicencia - 1)
                            },
                            enabled = uiState.diasLicencia > 1
                        ) {
                            Text("-", fontSize = MaterialTheme.typography.titleLarge.fontSize)
                        }
                        Text(
                            text = "${uiState.diasLicencia}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        IconButton(
                            onClick = { 
                                viewModel.updateDiasLicencia(uiState.diasLicencia + 1)
                            }
                        ) {
                            Text("+", fontSize = MaterialTheme.typography.titleLarge.fontSize)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 5. Vendedores
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "5. Vendedores (Opcional)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Los vendedores podrán iniciar sesión en la App Cliente con su Android ID",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("Cantidad de vendedores:")
                        IconButton(
                            onClick = { 
                                viewModel.updateCantidadVendedores(uiState.cantidadVendedores - 1)
                            },
                            enabled = uiState.cantidadVendedores > 0
                        ) {
                            Text("-", fontSize = MaterialTheme.typography.titleLarge.fontSize)
                        }
                        Text(
                            text = "${uiState.cantidadVendedores}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        IconButton(
                            onClick = { 
                                viewModel.updateCantidadVendedores(uiState.cantidadVendedores + 1)
                            }
                        ) {
                            Text("+", fontSize = MaterialTheme.typography.titleLarge.fontSize)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    uiState.vendedores.forEachIndexed { index, vendedor ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp)
                            ) {
                                Text(
                                    text = "Vendedor #${index + 1}",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(4.dp))

                                OutlinedTextField(
                                    value = vendedor.nombre,
                                    onValueChange = { nuevoNombre ->
                                        viewModel.updateVendedor(index, nuevoNombre, vendedor.usuario, vendedor.password, vendedor.androidId, vendedor.localSeleccionado)
                                    },
                                    label = { Text("Nombre completo *") },
                                    modifier = Modifier.fillMaxWidth(),
                                    isError = uiState.error != null && vendedor.nombre.isBlank()
                                )
                                Spacer(modifier = Modifier.height(4.dp))

                                OutlinedTextField(
                                    value = vendedor.usuario,
                                    onValueChange = { nuevoUsuario ->
                                        viewModel.updateVendedor(index, vendedor.nombre, nuevoUsuario, vendedor.password, vendedor.androidId, vendedor.localSeleccionado)
                                    },
                                    label = { Text("Usuario *") },
                                    modifier = Modifier.fillMaxWidth(),
                                    isError = uiState.error != null && vendedor.usuario.isBlank()
                                )
                                Spacer(modifier = Modifier.height(4.dp))

                                OutlinedTextField(
                                    value = vendedor.password,
                                    onValueChange = { nuevoPassword ->
                                        viewModel.updateVendedor(index, vendedor.nombre, vendedor.usuario, nuevoPassword, vendedor.androidId, vendedor.localSeleccionado)
                                    },
                                    label = { Text("Contraseña *") },
                                    modifier = Modifier.fillMaxWidth(),
                                    isError = uiState.error != null && vendedor.password.isBlank()
                                )
                                Spacer(modifier = Modifier.height(4.dp))

                                OutlinedTextField(
                                    value = vendedor.androidId,
                                    onValueChange = { nuevoAndroidId ->
                                        viewModel.updateVendedor(index, vendedor.nombre, vendedor.usuario, vendedor.password, nuevoAndroidId, vendedor.localSeleccionado)
                                    },
                                    label = { Text("Android ID *") },
                                    modifier = Modifier.fillMaxWidth(),
                                    placeholder = { Text("G360-XXXXXXXXXXXX") },
                                    isError = uiState.error != null && vendedor.androidId.isBlank()
                                )
                                Spacer(modifier = Modifier.height(4.dp))

                                var expanded by remember { mutableStateOf(false) }
                                ExposedDropdownMenuBox(
                                    expanded = expanded,
                                    onExpandedChange = { expanded = it }
                                ) {
                                    OutlinedTextField(
                                        value = vendedor.localSeleccionado,
                                        onValueChange = {},
                                        readOnly = true,
                                        label = { Text("Asignar local *") },
                                        modifier = Modifier.fillMaxWidth().menuAnchor(),
                                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                                        isError = uiState.error != null && vendedor.localSeleccionado.isBlank()
                                    )
                                    ExposedDropdownMenu(
                                        expanded = expanded,
                                        onDismissRequest = { expanded = false }
                                    ) {
                                        uiState.sucursales.forEachIndexed { sucursalIndex, sucursal ->
                                            DropdownMenuItem(
                                                text = { Text("${sucursal.nombre} (Sucursal ${sucursalIndex + 1})") },
                                                onClick = {
                                                    viewModel.updateVendedor(
                                                        index,
                                                        vendedor.nombre,
                                                        vendedor.usuario,
                                                        vendedor.password,
                                                        vendedor.androidId,
                                                        (sucursalIndex + 1).toString()
                                                    )
                                                    expanded = false
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (uiState.error != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "❌ ${uiState.error}",
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.weight(1f)
                        )
                        TextButton(onClick = viewModel::limpiarError) {
                            Text("OK")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onBack,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary
                    )
                ) {
                    Text("Cancelar")
                }
                Button(
                    onClick = { viewModel.registrarCliente() },
                    modifier = Modifier.weight(2f),
                    enabled = !uiState.isLoading
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = Color.White
                        )
                    } else {
                        Text("✅ Crear Cliente")
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
