package com.admin360.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Inbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.admin360.ui.theme.AppSpacing

@Composable
fun EmptyState(

    title: String,

    subtitle: String

) {

    Column(

        modifier = Modifier.fillMaxSize(),

        verticalArrangement = Arrangement.Center,

        horizontalAlignment = Alignment.CenterHorizontally

    ) {

        Icon(

            Icons.Rounded.Inbox,

            null

        )

        Spacer(Modifier.height(AppSpacing.md))

        Text(

            title,

            style = MaterialTheme.typography.titleLarge

        )

        Spacer(Modifier.height(AppSpacing.sm))

        Text(

            subtitle,

            style = MaterialTheme.typography.bodyMedium

        )

    }

}
