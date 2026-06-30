package com.admin360.feature.dashboard.viewmodel

import androidx.lifecycle.viewModelScope
import com.admin360.core.base.BaseViewModel
import com.admin360.feature.dashboard.data.DashboardRepository
import com.admin360.feature.dashboard.model.DashboardState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val repo: DashboardRepository
) : BaseViewModel() {

    private val _state = MutableStateFlow(DashboardState())
    val state = _state.asStateFlow()

    fun loadDashboard() {

        viewModelScope.launch {

            try {

                _state.value = _state.value.copy(loading = true)

                val negocios = repo.getNegociosCount()
                val locales = repo.getLocalesCount()
                val usuarios = repo.getUsuariosCount()
                val licencias = repo.getLicenciasActivasCount()

                _state.value = DashboardState(
                    negocios = negocios,
                    locales = locales,
                    usuarios = usuarios,
                    licenciasActivas = licencias,
                    loading = false
                )

            } catch (e: Exception) {

                _state.value = _state.value.copy(
                    loading = false,
                    error = e.message
                )
            }
        }
    }
}
