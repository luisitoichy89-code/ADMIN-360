package com.admin360.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.admin360.core.session.SessionManager
import com.admin360.core.session.SessionData
import com.admin360.core.session.UserRole
import com.admin360.feature.auth.viewmodel.LoginViewModel
import com.admin360.core.auth.AuthRepository
import com.admin360.core.device.DeviceInfo
import androidx.compose.ui.platform.LocalContext

@Composable
fun LoginScreen(
    onSuccess: () -> Unit
) {
    val context = LocalContext.current
    val androidId = DeviceInfo.getAndroidId(context)

    val viewModel: LoginViewModel = viewModel(
        factory = object : androidx.lifecycle.ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return LoginViewModel(AuthRepository()) as T
            }
        }
    )

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Admin360",
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(Modifier.height(24.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(20.dp))

        if (viewModel.error.value != null) {
            Text(
                text = viewModel.error.value!!,
                color = MaterialTheme.colorScheme.error
            )
        }

        Spacer(Modifier.height(12.dp))

        Button(
            onClick = {
                isLoading = true
                viewModel.login(email, password) {
                    isLoading = false
                    onSuccess()
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(18.dp),
                    strokeWidth = 2.dp
                )
            } else {
                Text("Entrar")
            }
        }
    }
}
