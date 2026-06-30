package com.admin360.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    title: String,
    onMenuClick: () -> Unit,
    onSearchClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {}
) {

    CenterAlignedTopAppBar(

        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge
            )
        },

        navigationIcon = {

            IconButton(
                onClick = onMenuClick
            ) {
                Icon(
                    Icons.Rounded.Menu,
                    null
                )
            }

        },

        actions = {

            IconButton(onClick = onSearchClick) {
                Icon(Icons.Rounded.Search, null)
            }

            IconButton(onClick = onNotificationClick) {
                Icon(Icons.Rounded.Notifications, null)
            }

        }

    )

}
