package com.admin360.core.device

import com.admin360.core.session.SessionManager

object DeviceValidator {

    fun isValid(androidId: String): Boolean {
        return SessionManager.session.value.androidId == androidId
    }
}
