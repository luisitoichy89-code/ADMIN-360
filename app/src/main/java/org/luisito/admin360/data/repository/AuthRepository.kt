package org.luisito.admin360.data.repository

import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import org.luisito.admin360.data.SupabaseClientProvider
import org.luisito.admin360.data.models.LoginResult
import org.luisito.admin360.data.models.User
import java.security.MessageDigest
import java.time.LocalDate

class AuthRepository {

    suspend fun login(username: String, password: String): LoginResult {
        return try {
            val supabase = SupabaseClientProvider.client
            
            val users = supabase.postgrest.from("usuarios")
                .select(Columns.ALL) {
                    filter { eq("username", username) }
                }
                .decodeAs<List<User>>()

            if (users.isEmpty()) {
                return LoginResult.Error("Usuario no encontrado")
            }

            val user = users.first()
            
            val storedHash = user.password ?: ""
            val inputHash = hash(password)

            if (storedHash != inputHash && storedHash.isNotEmpty()) {
                return LoginResult.Error("Contraseña incorrecta")
            }

            if (!user.activo) {
                return LoginResult.Error("Usuario desactivado")
            }

            // SUPERADMIN BYPASS - NO llama a RPC
            if (user.rol == "superadmin") {
                return LoginResult.Success(user.id, user)
            }

            // SOLO para admin_almacen y seller se verifica licencia
            val licencias = supabase.postgrest.from("licencias")
                .select(Columns.ALL) {
                    filter { 
                        eq("cliente_id", user.cliente_id) 
                        eq("activo", true)
                    }
                }
                .decodeAs<List<Map<String, Any>>>()
            
            if (licencias.isEmpty()) {
                return LoginResult.Error("Licencia no encontrada")
            }
            
            val expiracion = licencias.first()["expiracion"] as? String
            if (expiracion != null) {
                val expDate = LocalDate.parse(expiracion)
                val now = LocalDate.now()
                if (expDate.isBefore(now)) {
                    return LoginResult.Error("Licencia expirada")
                }
            }

            LoginResult.Success(user.id, user)

        } catch (e: Exception) {
            LoginResult.Error(e.message ?: "Error de conexión")
        }
    }

    private fun hash(password: String): String {
        val bytes = MessageDigest.getInstance("SHA-256")
            .digest(password.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

    suspend fun sendPasswordRecovery(email: String): Boolean {
        return false
    }
}
