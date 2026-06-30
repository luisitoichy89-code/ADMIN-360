package com.admin360.ui.components

import androidx.compose.material3.*

import androidx.compose.runtime.Composable

@Composable
fun ConfirmationDialog(

    title: String,

    message: String,

    onConfirm: () -> Unit,

    onDismiss: () -> Unit

) {

    AlertDialog(

        onDismissRequest = onDismiss,

        confirmButton = {

            Button(

                onClick = onConfirm

            ) {

                Text("Aceptar")

            }

        },

        dismissButton = {

            OutlinedButton(

                onClick = onDismiss

            ) {

                Text("Cancelar")

            }

        },

        title = {

            Text(title)

        },

        text = {

            Text(message)

        }

    )

}
