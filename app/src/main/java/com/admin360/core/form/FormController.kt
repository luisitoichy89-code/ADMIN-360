package com.admin360.core.form

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateOf

class FormController : ViewModel() {

    var state by mutableStateOf(FormState())
        private set

    fun <T> submit(
        action: suspend () -> T,
        onSuccess: (T) -> Unit
    ) {
        viewModelScope.launch {
            state = state.copy(loading = true, error = null)
            try {
                val result = action()
                state = state.copy(loading = false, success = true)
                onSuccess(result)
            } catch (e: Exception) {
                state = state.copy(
                    loading = false,
                    error = e.message ?: "Error desconocido"
                )
            }
        }
    }

    fun reset() {
        state = FormState()
    }
}
