package com.admin360.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SearchBar(

    query: String,

    onQueryChange: (String) -> Unit

) {

    OutlinedTextField(

        modifier = Modifier.fillMaxWidth(),

        value = query,

        onValueChange = onQueryChange,

        singleLine = true,

        leadingIcon = {

            Icon(Icons.Rounded.Search, null)

        },

        trailingIcon = {

            if (query.isNotEmpty()) {

                IconButton(

                    onClick = {

                        onQueryChange("")

                    }

                ) {

                    Icon(Icons.Rounded.Close, null)

                }

            }

        },

        label = {

            Text("Buscar")

        }

    )

}
