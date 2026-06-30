package com.admin360.core.security

import com.admin360.core.model.LicenseDto
import com.admin360.core.session.SessionManager
import com.admin360.core.supabase.SupabaseModule
import io.github.jan.supabase.postgrest.from

object LicenseGuard {

    private var cachedValid: Boolean? = null

    suspend fun validateOrBlock(): Boolean {

        val session = SessionManager.session.value

        if (!session.logged) return false

        val clienteId = session.clienteId

        return try {

            val license = SupabaseModule.client
                .from("licencias")
                .select {
                    filter {
                        eq("cliente_id", clienteId)
                    }
                }
                .decodeSingleOrNull<LicenseDto>()

            val isValid = license?.estado == "ACTIVA"

            cachedValid = isValid

            isValid

        } catch (e: Exception) {

            cachedValid ?: false
        }
    }

    fun isBlocked(): Boolean = cachedValid == false

    fun isAllowed(): Boolean = cachedValid == true
}
