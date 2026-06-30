package com.admin360.core.security

object SecurityGate {

    fun canAccess(
        logged: Boolean,
        licenseOk: Boolean,
        deviceOk: Boolean
    ): Boolean {

        return logged && licenseOk && deviceOk
    }
}
