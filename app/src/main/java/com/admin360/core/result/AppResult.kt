package com.admin360.core.result

sealed class AppResult<out T> {

    data class Success<T>(
        val data: T
    ) : AppResult<T>()

    data class Error(
        val message: String,
        val exception: Throwable? = null
    ) : AppResult<Nothing>()

    object Loading : AppResult<Nothing>()

}
