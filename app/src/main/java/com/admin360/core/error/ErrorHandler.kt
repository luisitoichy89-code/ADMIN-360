package com.admin360.core.error

object ErrorHandler {

    fun parse(error: Throwable): String {
        return when (error) {
            is io.ktor.client.plugins.ResponseException ->
                "Error de servidor"

            is java.net.UnknownHostException ->
                "Sin conexión a internet"

            else -> error.message ?: "Error desconocido"
        }
    }
}
