package org.luisito.admin360.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.luisito.admin360.ui.components.AdminNavigationDrawer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
    onNegociosClick: () -> Unit,
    onLocalesClick: () -> Unit,
    onUsuariosClick: () -> Unit,
    onLicenciasClick: () -> Unit,
    onTrazasClick: () -> Unit,
    onBuscadorClick: () -> Unit,
    onRegistroClienteClick: () -> Unit,
    onCerrarSesion: () -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AdminNavigationDrawer(
                onNegociosClick = {
                    scope.launch { drawerState.close() }
                    onNegociosClick()
                },
                onLocalesClick = {
                    scope.launch { drawerState.close() }
                    onLocalesClick()
                },
                onUsuariosClick = {
                    scope.launch { drawerState.close() }
                    onUsuariosClick()
                },
                onLicenciasClick = {
                    scope.launch { drawerState.close() }
                    onLicenciasClick()
                },
                onTrazasClick = {
                    scope.launch { drawerState.close() }
                    onTrazasClick()
                },
                onCerrarSesion = {
                    scope.launch { drawerState.close() }
                    onCerrarSesion()
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Panel de Control") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menú", tint = Color.White)
                        }
                    },
                    actions = {
                        IconButton(onClick = onRegistroClienteClick) {
                            Icon(Icons.Default.Add, contentDescription = "Crear", tint = Color.White)
                        }
                        IconButton(onClick = onBuscadorClick) {
                            Icon(Icons.Default.Search, contentDescription = "Buscador", tint = Color.White)
                        }
                        IconButton(onClick = onCerrarSesion) {
                            Icon(Icons.Default.ExitToApp, contentDescription = "Cerrar sesión", tint = Color.White)
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
                    .padding(16.dp)
            ) {
                Text(
                    text = "Bienvenido, Administrador",
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = "Usa el buscador 🔍 para encontrar cualquier elemento",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 8.dp)
                )
                Text(
                    text = "O usa el menú ☰ para navegar",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}
