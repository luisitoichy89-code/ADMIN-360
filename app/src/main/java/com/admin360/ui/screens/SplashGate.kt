package com.admin360.ui.screens

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay

@Composable
fun SplashGate(message: String = "Cargando...") {

    var loading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(600)
        loading = false
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        if (loading) {
            CircularProgressIndicator()
        } else {
            Text(message)
        }
    }
}
