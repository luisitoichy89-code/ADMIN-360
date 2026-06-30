package com.admin360.core.session

object SessionAccess {

    fun isLogged(): Boolean {
        return SessionManager.session.value.logged
    }

    fun getRole(): UserRole {
        return SessionManager.session.value.rol
    }
}
