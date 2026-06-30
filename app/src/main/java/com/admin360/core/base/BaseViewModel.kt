package com.admin360.core.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class BaseViewModel : ViewModel() {

    protected val _loading =
        MutableStateFlow(false)

    val loading =
        _loading.asStateFlow()

    protected val _message =
        MutableStateFlow<String?>(null)

    val message =
        _message.asStateFlow()

    fun showLoading() {

        _loading.value = true

    }

    fun hideLoading() {

        _loading.value = false

    }

    fun sendMessage(text: String) {

        _message.value = text

    }

}
