package com.admin360.feature.dashboard.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.admin360.feature.dashboard.data.DashboardRepository
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateOf

class DashboardViewModel(
    private val repo: DashboardRepository
) : ViewModel() {

    var negocios = mutableStateOf(0)
    var locales = mutableStateOf(0)
    var usuarios = mutableStateOf(0)
    var licenciasActivas = mutableStateOf(0)

    fun load(clienteId: String) {

        viewModelScope.launch {
            negocios.value = repo.getNegociosCount(clienteId)
            locales.value = repo.getLocalesCount(clienteId)
            usuarios.value = repo.getUsuariosCount(clienteId)
            licenciasActivas.value = repo.getLicenciasActivasCount(clienteId)
        }
    }
}
