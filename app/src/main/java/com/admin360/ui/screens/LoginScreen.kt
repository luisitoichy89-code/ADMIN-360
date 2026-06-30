package com.admin360.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.admin360.core.session.SessionManager
import com.admin360.feature.auth.viewmodel.LoginViewModel
import com.admin360.core.auth.AuthRepository
import com.admin360.feature.user.data.UserRepository
import com.admin360.core.device.DeviceInfo
import androidx.compose.ui.platform.LocalContext

@Composable
fun LoginScreen(
    onSuccess: () -> Unit
) {

    val context = LocalContext.current
    val androidId = DeviceInfo.getAndroidId(context)

    val viewModel = LoginViewModel(
        authRepo = AuthRepository(),
        userRepo = UserRepository()
    )

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {

        Text("Login Admin360")

        Spacer(Modifier.height(16.dp))

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

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = {
                viewModel.login(email, password, androidId)
                onSuccess()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Entrar")
        }
    }
}
