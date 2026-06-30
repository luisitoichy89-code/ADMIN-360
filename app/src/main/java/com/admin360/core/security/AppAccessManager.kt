package com.admin360.core.security

object AppAccessManager {

    fun canEnter(
        licenseOk: Boolean,
        deviceOk: Boolean,
        sessionOk: Boolean
    ): Boolean {
        return licenseOk && deviceOk && sessionOk
    }
}
