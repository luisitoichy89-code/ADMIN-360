package com.admin360.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun KpiCard(
    title: String,
    value: String
) {

    Card(
        modifier = Modifier
            .width(160.dp)
            .height(110.dp)
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(Modifier.height(6.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
