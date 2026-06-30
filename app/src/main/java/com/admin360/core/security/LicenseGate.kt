package com.admin360.core.security

import com.admin360.core.model.LicenseDto

object LicenseGate {

    fun isValid(license: LicenseDto?): Boolean {
        return license?.estado == "ACTIVA"
    }

    fun isExpired(license: LicenseDto?): Boolean {
        return license?.estado != "ACTIVA"
    }
}
