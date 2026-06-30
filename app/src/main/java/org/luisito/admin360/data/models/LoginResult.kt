package org.luisito.admin360.data.models

sealed class LoginResult {
    data class Success(val userId: String, val user: User) : LoginResult()
    data class Error(val message: String) : LoginResult()
}
