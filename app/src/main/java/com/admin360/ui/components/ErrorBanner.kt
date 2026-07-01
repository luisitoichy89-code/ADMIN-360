package com.admin360.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun ErrorBanner(error: String?, onRetry: () -> Unit) {
    if (error == null) return

    Card(
        colors = CardDefaults.cardColors(containerColor = Color.Red.copy(alpha = 0.1f))
    ) {
        Row {
            Text(error, color = Color.Red)
            Button(onClick = onRetry) {
                Text("Reintentar")
            }
        }
    }
}
