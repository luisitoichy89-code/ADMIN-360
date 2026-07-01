package com.admin360.ui.forms

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.admin360.feature.negocios.model.NegocioDto

@Composable
fun NegocioForm(
    onSave: (NegocioDto) -> Unit
) {

    var nombre by remember { mutableStateOf("") }
    var propietario by remember { mutableStateOf("") }

    Column {
        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre negocio") }
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = propietario,
            onValueChange = { propietario = it },
            label = { Text("Propietario") }
        )

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                if (nombre.isBlank() || propietario.isBlank()) return@Button
                onSave(
                    NegocioDto(
                        nombre = nombre,
                        propietario = propietario
                    )
                )
            }
        ) {
            Text("Guardar")
        }
    }
}
