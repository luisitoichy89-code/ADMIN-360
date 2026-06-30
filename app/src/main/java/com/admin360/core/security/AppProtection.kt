package com.admin360.core.security

import com.admin360.core.session.SessionManager
import com.admin360.core.device.DeviceAntiTamper

object AppProtection {

    // 🔥 6.1 License hard lock
    fun enforceLicense(licenseActive: Boolean): Boolean {
        if (!licenseActive) {
            forceBlockApp()
            return false
        }
        return true
    }

    // 🔥 6.2 Device lock
    fun enforceDevice(savedAndroidId: String?, currentAndroidId: String): Boolean {
        if (!DeviceAntiTamper.isDeviceValid(savedAndroidId, currentAndroidId)) {
            SessionManager.logout()
            return false
        }
        return true
    }

    // 🔥 6.3 Session hijack protection
    fun enforceSession(sessionValid: Boolean): Boolean {
        if (!sessionValid) {
            SessionManager.logout()
            return false
        }
        return true
    }

    private fun forceBlockApp() {
        // Limpiar sesión y forzar bloqueo
        SessionManager.logout()
        // La UI debe mostrar BlockedScreen
    }
}
