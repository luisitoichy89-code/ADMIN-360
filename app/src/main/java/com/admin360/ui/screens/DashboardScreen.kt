package com.admin360.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.admin360.feature.dashboard.viewmodel.DashboardViewModel
import com.admin360.feature.logs.viewmodel.LogsViewModel
import com.admin360.ui.components.DashboardCard

@Composable
fun DashboardScreen(
    onNavigate: (String) -> Unit = {},
    dashboardVm: DashboardViewModel = viewModel(),
    logsVm: LogsViewModel = viewModel()
) {
    val dashboardState = dashboardVm.state
    val logsState = logsVm.state

    LaunchedEffect(Unit) {
        dashboardVm.load()
        logsVm.load()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Dashboard",
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(Modifier.height(16.dp))

        if (dashboardVm.loading) {
            CircularProgressIndicator()
            return@Column
        }

        Row {
            DashboardCard("Negocios", dashboardState.totalNegocios)
            DashboardCard("Locales", dashboardState.totalLocales)
        }

        Spacer(Modifier.height(8.dp))

        Row {
            DashboardCard("Usuarios", dashboardState.totalUsuarios)
            DashboardCard("Licencias OK", dashboardState.licenciasActivas)
        }

        Spacer(Modifier.height(8.dp))

        DashboardCard("Licencias Vencidas", dashboardState.licenciasVencidas, modifier = Modifier.fillMaxWidth())

        Spacer(Modifier.height(16.dp))

        Text(
            text = "Actividad Reciente",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(Modifier.height(8.dp))

        if (logsVm.loading) {
            CircularProgressIndicator()
        }

        LazyColumn {
            items(logsState.logs) { log ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(log.accion)
                        Text(log.created_at, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}
