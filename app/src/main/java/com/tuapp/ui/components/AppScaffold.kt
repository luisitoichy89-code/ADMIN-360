package com.tuapp.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.*
import androidx.compose.runtime.Composable

@Composable
fun AppScaffold(

    topBar: @Composable () -> Unit,

    drawerContent: @Composable () -> Unit,

    floatingActionButton: @Composable (() -> Unit)? = null,

    content: @Composable (PaddingValues) -> Unit

) {

    ModalNavigationDrawer(

        drawerContent = {

            ModalDrawerSheet {

                drawerContent()

            }

        }

    ) {

        Scaffold(

            topBar = topBar,

            floatingActionButton = {

                floatingActionButton?.invoke()

            }

        ) { padding ->

            content(padding)

        }

    }

}
