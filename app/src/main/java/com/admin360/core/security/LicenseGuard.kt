package com.admin360.core.security

import com.admin360.feature.license.data.LicenseRepository

object LicenseGuard {

    suspend fun validate(clienteId: String): Boolean {

        val license = LicenseRepository().getLicense(clienteId)

        return license?.estado == "ACTIVA"
    }
}
