package com.admin360.feature.dashboard.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.admin360.feature.dashboard.data.DashboardRepository
import com.admin360.feature.dashboard.model.DashboardDto
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateOf

class DashboardViewModel : ViewModel() {

    private val repo = DashboardRepository()

    var state by mutableStateOf(DashboardDto(0, 0, 0, 0, 0))
        private set

    var loading by mutableStateOf(false)

    fun load() {
        viewModelScope.launch {
            loading = true
            state = repo.getDashboard()
            loading = false
        }
    }
}
