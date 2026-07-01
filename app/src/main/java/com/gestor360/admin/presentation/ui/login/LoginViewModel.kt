package com.gestor360.admin.presentation.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gestor360.admin.domain.usecases.auth.LoginUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false
)

class LoginViewModel(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onUsernameChange(value: String) {
        _uiState.update {
            it.copy(
                username = value,
                error = null
            )
        }
    }

    fun onPasswordChange(value: String) {
        _uiState.update {
            it.copy(
                password = value,
                error = null
            )
        }
    }

    fun login() {
        viewModelScope.launch {

            val state = _uiState.value

            if (state.username.isBlank() || state.password.isBlank()) {
                _uiState.update {
                    it.copy(error = "Completa todos los campos.")
                }
                return@launch
            }

            _uiState.update { it.copy(isLoading = true, error = null) }

            val result = loginUseCase(
                username = state.username,
                password = state.password
            )

            result
                .onSuccess { usuario ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            success = true,
                            error = null
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            success = false,
                            error = error.message ?: "Error desconocido"
                        )
                    }
                }
        }
    }
}
