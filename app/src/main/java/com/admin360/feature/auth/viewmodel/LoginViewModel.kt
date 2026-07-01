package com.admin360.feature.auth.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.admin360.core.auth.AuthService
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateOf

class LoginViewModel : ViewModel() {

    var loading by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)

    private val auth = AuthService()

    fun login(email: String, password: String, context: Context, onSuccess: () -> Unit) {

        viewModelScope.launch {

            loading = true
            error = null

            val result = auth.login(email, password, context)

            loading = false

            if (result) {
                onSuccess()
            } else {
                error = "Error de login o licencia inválida"
            }
        }
    }
}
