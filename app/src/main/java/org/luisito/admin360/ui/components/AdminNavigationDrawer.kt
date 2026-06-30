package org.luisito.admin360.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AdminNavigationDrawer(
    onNegociosClick: () -> Unit,
    onLocalesClick: () -> Unit,
    onUsuariosClick: () -> Unit,
    onLicenciasClick: () -> Unit,
    onTrazasClick: () -> Unit,
    onCerrarSesion: () -> Unit
) {
    ModalDrawerSheet {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Gestor360 Admin",
                style = androidx.compose.material3.MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Divider()

            NavigationDrawerItem(
                label = { Text("🏢 Negocios") },
                selected = false,
                onClick = onNegociosClick
            )
            NavigationDrawerItem(
                label = { Text("📍 Locales") },
                selected = false,
                onClick = onLocalesClick
            )
            NavigationDrawerItem(
                label = { Text("👥 Usuarios") },
                selected = false,
                onClick = onUsuariosClick
            )
            NavigationDrawerItem(
                label = { Text("📜 Licencias") },
                selected = false,
                onClick = onLicenciasClick
            )
            NavigationDrawerItem(
                label = { Text("📋 Trazas") },
                selected = false,
                onClick = onTrazasClick
            )
            Divider()
        }
    }
}
