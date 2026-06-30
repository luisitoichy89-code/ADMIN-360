package com.tuapp.core

import androidx.compose.material3.DrawerState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class AppState(

    val drawerState: DrawerState

) {

    var loading by mutableStateOf(false)

    var currentTitle by mutableStateOf("Dashboard")

    var searchQuery by mutableStateOf("")

    fun showLoading() {
        loading = true
    }

    fun hideLoading() {
        loading = false
    }

    fun updateTitle(title: String) {
        currentTitle = title
    }

}
