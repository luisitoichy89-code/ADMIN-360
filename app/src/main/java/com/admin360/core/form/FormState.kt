package com.admin360.core.form

data class FormState(
    val loading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false
)
