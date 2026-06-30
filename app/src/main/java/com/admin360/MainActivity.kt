package com.admin360

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.navigation.compose.rememberNavController
import com.admin360.core.security.SecurityManager
import com.admin360.core.session.SessionManager
import com.admin360.ui.navigation.AppNavHost
import com.admin360.ui.screens.SplashGate
import com.admin360.ui.theme.Admin360Theme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            Admin360Theme {

                val navController = rememberNavController()

                val session by SessionManager.session.collectAsState()

                var isReady by remember { mutableStateOf(false) }

                /**
                 * SPLASH GATE:
                 * Decide si la app puede entrar o no
                 */
                LaunchedEffect(session.logged) {

                    // Simula inicialización (Supabase, cache, etc)
                    if (!isReady) {

                        kotlinx.coroutines.delay(600)

                        isReady = true
                    }
                }

                when {

                    // 1. Splash inicial
                    !isReady -> {

                        SplashGate()

                    }

                    // 2. Bloqueo por seguridad
                    isReady && !SecurityManager.canLogin() -> {

                        SplashGate(
                            message = "Acceso denegado. Verifica licencia o dispositivo."
                        )

                    }

                    // 3. App normal
                    else -> {

                        AppNavHost(
                            navController = navController,
                            startDestination = resolveStartDestination()
                        )

                    }
                }
            }
        }
    }

    /**
     * Decide a dónde entra el usuario según sesión
     */
    private fun resolveStartDestination(): String {

        val session = SessionManager.session.value

        return when {

            !session.logged -> "login"

            session.rol.name == "SUPER_ADMIN" -> "dashboard"

            session.licenciaActiva -> "dashboard"

            else -> "login"
        }
    }
}
