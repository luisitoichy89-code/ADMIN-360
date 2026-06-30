package com.admin360.core.security

object DeviceGuard {

    fun canAccess(androidId: String): Boolean {

        return DeviceValidator.isDeviceValid(androidId)

    }

}
