package com.admin360.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.admin360.feature.dashboard.viewmodel.DashboardViewModel
import com.admin360.ui.components.KpiCard

@Composable
fun DashboardScreen(
    onNavigate: (String) -> Unit,
    viewModel: DashboardViewModel = viewModel()
) {

    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadDashboard()
    }

    if (state.loading) {
        CircularProgressIndicator()
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Dashboard",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            KpiCard("Negocios", state.negocios.toString())
            KpiCard("Locales", state.locales.toString())
        }

        Spacer(Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            KpiCard("Usuarios", state.usuarios.toString())
            KpiCard("Licencias", state.licenciasActivas.toString())
        }

        Spacer(Modifier.height(20.dp))

        Button(
            onClick = { onNavigate("negocios") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Gestionar negocios")
        }

        Spacer(Modifier.height(8.dp))

        Button(
            onClick = { onNavigate("locales") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Gestionar locales")
        }

        Spacer(Modifier.height(8.dp))

        Button(
            onClick = { onNavigate("usuarios") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Gestionar usuarios")
        }

        Spacer(Modifier.height(8.dp))

        Button(
            onClick = { onNavigate("licencias") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Gestionar licencias")
        }
    }
}
