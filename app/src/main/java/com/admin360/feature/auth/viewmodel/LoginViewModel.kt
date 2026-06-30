package com.admin360.feature.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.admin360.core.auth.AuthRepository
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateOf

class LoginViewModel(
    private val authRepo: AuthRepository
) : ViewModel() {

    var error = mutableStateOf<String?>(null)

    fun login(email: String, password: String, onSuccess: () -> Unit) {

        viewModelScope.launch {
            try {
                authRepo.login(email, password)
                error.value = null
                onSuccess()
            } catch (e: Exception) {
                error.value = e.message ?: "Error de login"
            }
        }
    }
}
