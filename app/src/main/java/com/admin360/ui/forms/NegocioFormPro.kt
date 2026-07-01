package com.admin360.ui.forms

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.admin360.core.form.FormController
import com.admin360.core.validation.Validator
import com.admin360.feature.negocios.model.NegocioDto
import com.admin360.ui.components.FormDialog

@Composable
fun NegocioFormPro(
    controller: FormController,
    onSave: (NegocioDto) -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var propietario by remember { mutableStateOf("") }

    val state = controller.state

    FormDialog(
        title = "Crear Negocio",
        show = true,
        loading = state.loading,
        error = state.error,
        onDismiss = { controller.reset() },
        onConfirm = {
            if (!Validator.required(nombre)) return@FormDialog
            if (!Validator.required(propietario)) return@FormDialog

            controller.submit(
                action = {
                    NegocioDto(nombre = nombre, propietario = propietario)
                },
                onSuccess = onSave
            )
        }
    ) {
        Column {
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre") }
            )
            OutlinedTextField(
                value = propietario,
                onValueChange = { propietario = it },
                label = { Text("Propietario") }
            )
        }
    }
}
