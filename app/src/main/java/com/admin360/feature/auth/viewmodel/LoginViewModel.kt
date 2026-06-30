package com.admin360.feature.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.admin360.core.auth.AuthRepository
import com.admin360.core.session.SessionManager
import com.admin360.core.session.SessionData
import com.admin360.core.session.UserRole
import com.admin360.core.supabase.SupabaseModule
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateOf

class LoginViewModel(
    private val authRepo: AuthRepository
) : ViewModel() {

    var error = mutableStateOf<String?>(null)

    fun login(email: String, password: String, onSuccess: () -> Unit) {

        viewModelScope.launch {

            try {

                error.value = null

                // 1. Login en Supabase
                authRepo.login(email, password)

                // 2. Obtener usuario autenticado
                val user = SupabaseModule.client.auth.currentUserOrNull()

                if (user == null) {
                    error.value = "Usuario no encontrado"
                    return@launch
                }

                // 3. Crear sesión (sin profile por ahora)
                val session = SessionData(
                    logged = true,
                    usuarioId = user.id,
                    negocioId = "",
                    clienteId = "",
                    email = email,
                    rol = UserRole.SUPER_ADMIN,
                    androidId = "",
                    licenciaActiva = true
                )

                SessionManager.login(session)

                onSuccess()

            } catch (e: Exception) {
                error.value = e.message ?: "Error de login"
            }
        }
    }
}
