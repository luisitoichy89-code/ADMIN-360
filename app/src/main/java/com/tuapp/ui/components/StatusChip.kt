package com.tuapp.ui.components

import androidx.compose.material3.AssistChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun StatusChip(

    text: String

) {

    AssistChip(

        onClick = {},

        label = {

            Text(text)

        }

    )

}
