package com.admin360.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DashboardCard(

    title: String,

    subtitle: String,

    onClick: () -> Unit

) {

    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {

        Column(
            modifier = Modifier.padding(20.dp)
        ) {

            Text(
                title,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(
                Modifier.height(8.dp)
            )

            Text(
                subtitle,
                style = MaterialTheme.typography.bodyMedium
            )

        }

    }

}
