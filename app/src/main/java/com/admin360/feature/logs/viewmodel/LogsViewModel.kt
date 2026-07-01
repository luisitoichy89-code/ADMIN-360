package com.admin360.feature.logs.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.admin360.feature.logs.data.LogsRepository
import com.admin360.feature.logs.model.LogDto
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateOf

data class LogsState(
    val loading: Boolean = false,
    val logs: List<LogDto> = emptyList(),
    val error: String? = null
)

class LogsViewModel : ViewModel() {

    private val repo = LogsRepository()

    var state by mutableStateOf(LogsState())
        private set

    var loading by mutableStateOf(false)

    fun load() {
        viewModelScope.launch {
            loading = true
            try {
                val data = repo.getRecent()
                state = state.copy(logs = data)
            } catch (e: Exception) {
                state = state.copy(error = e.message)
            }
            loading = false
        }
    }
}
