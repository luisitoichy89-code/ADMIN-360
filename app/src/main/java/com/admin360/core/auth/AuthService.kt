package com.admin360.core.auth

import android.content.Context
import com.admin360.core.device.DeviceManager
import com.admin360.core.session.SessionManager
import com.admin360.core.supabase.SupabaseModule
import com.admin360.feature.licencias.model.LicenciaDto
import com.admin360.feature.usuarios.model.UsuarioDto
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.postgrest.from

class AuthService {

    private val client = SupabaseModule.client

    suspend fun login(email: String, password: String, context: Context): Boolean {

        return try {

            val deviceId = DeviceManager.getDeviceId(context)

            // 1. Login en Supabase Auth
            client.auth.signInWith(Email) {
                this.email = email
                this.password = password
            }

            // 2. Obtener usuario
            val user = client.from("usuarios")
                .select {
                    filter { eq("email", email) }
                }
                .decodeList<UsuarioDto>()
                .firstOrNull()

            if (user == null) return false

            // 3. Obtener licencia
            val licencia = client.from("licencias")
                .select {
                    filter { eq("negocio_id", user.negocio_id) }
                }
                .decodeList<LicenciaDto>()
                .firstOrNull()

            if (licencia == null || licencia.estado != "ACTIVA") {
                throw Exception("Licencia inválida")
            }

            // 4. Device Binding
            if (licencia.android_id == null) {
                client.from("licencias")
                    .update({
                        set("android_id", deviceId)
                    }) {
                        filter { eq("id", licencia.id) }
                    }
            } else if (licencia.android_id != deviceId) {
                throw Exception("Dispositivo no autorizado")
            }

            // 5. Guardar sesión
            SessionManager.user = user
            SessionManager.negocioId = user.negocio_id
            SessionManager.licenciaActiva = true
            SessionManager.deviceId = deviceId

            true

        } catch (e: Exception) {
            false
        }
    }
}
