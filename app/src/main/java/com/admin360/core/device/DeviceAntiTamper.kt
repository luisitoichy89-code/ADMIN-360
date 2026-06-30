package com.admin360.core.device

import com.admin360.core.session.SessionManager

object DeviceAntiTamper {

    fun isDeviceValid(savedAndroidId: String?, currentAndroidId: String): Boolean {
        return savedAndroidId == null || savedAndroidId == currentAndroidId
    }

    fun checkAndForceLogout(savedAndroidId: String?, currentAndroidId: String): Boolean {
        if (!isDeviceValid(savedAndroidId, currentAndroidId)) {
            SessionManager.logout()
            return false
        }
        return true
    }
}
