package com.admin360.ui.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PrimaryButton(

    text: String,

    onClick: () -> Unit,

    enabled: Boolean = true

) {

    Button(

        modifier = Modifier
            .fillMaxWidth()
            .height(54.dp),

        enabled = enabled,

        onClick = onClick

    ) {

        Text(text)

    }

}
