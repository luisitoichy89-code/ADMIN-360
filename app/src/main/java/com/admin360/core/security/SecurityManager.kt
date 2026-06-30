package com.admin360.core.security

import com.admin360.core.session.SessionManager
import com.admin360.core.session.UserRole

object SecurityManager {

    val session
        get() = SessionManager.session.value

    fun canLogin(): Boolean {
        return session.licenciaActiva &&
                session.androidId.isNotBlank() &&
                session.logged
    }

    fun canAccessDashboard(): Boolean {
        return session.logged
    }

    fun canAccessLicencias(): Boolean {
        return session.rol == UserRole.SUPER_ADMIN
    }

    fun canCreateBusiness(): Boolean {
        return session.rol == UserRole.SUPER_ADMIN
    }

    fun canCreateLocal(): Boolean {
        return session.rol == UserRole.SUPER_ADMIN ||
                session.rol == UserRole.ADMIN_NEGOCIO
    }

    fun canCreateAdmin(): Boolean {
        return session.rol == UserRole.SUPER_ADMIN
    }

    fun canCreateSeller(): Boolean {
        return session.rol == UserRole.ADMIN_NEGOCIO
    }

    fun canDeleteUsers(): Boolean {
        return session.rol == UserRole.SUPER_ADMIN
    }

    fun canEditLicense(): Boolean {
        return session.rol == UserRole.SUPER_ADMIN
    }

    fun isSeller() = session.rol == UserRole.VENDEDOR

    fun isBusinessAdmin() = session.rol == UserRole.ADMIN_NEGOCIO

    fun isSuperAdmin() = session.rol == UserRole.SUPER_ADMIN
}
