package com.admin360.core.security

import com.admin360.core.session.SessionManager

object DeviceValidator {

    fun isDeviceValid(currentAndroidId: String): Boolean {

        val session = SessionManager.session.value

        if (!session.logged) return false

        return session.androidId == currentAndroidId
    }

}
