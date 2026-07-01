package com.admin360.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.admin360.feature.logs.model.LogDto

@Composable
fun RecentActivity(logs: List<LogDto>) {
    Column {
        Text("Actividad reciente")
        Spacer(modifier = Modifier.height(8.dp))
        logs.take(5).forEach { log ->
            Card {
                Column {
                    Text(log.accion)
                    Text(log.created_at)
                    if (log.usuario != null) {
                        Text("Usuario: ${log.usuario}")
                    }
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}
