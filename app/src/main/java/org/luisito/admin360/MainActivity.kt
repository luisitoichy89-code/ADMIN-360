package org.luisito.admin360

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.luisito.admin360.ui.theme.Admin360Theme
import org.luisito.admin360.ui.screens.*
import org.luisito.admin360.session.SessionManager

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            Admin360Theme {

                val navController = rememberNavController()
                val sessionManager: SessionManager = viewModel()

                val startDestination = if (sessionManager.isLoggedIn()) {
                    "dashboard"
                } else {
                    "login"
                }

                NavHost(
                    navController = navController,
                    startDestination = startDestination
                ) {

                    composable("login") {
                        LoginScreen(
                            onLoginSuccess = {
                                navController.navigate("dashboard") {
                                    popUpTo("login") { inclusive = true }
                                }
                            }
                        )
                    }

                    composable("dashboard") {
                        AdminDashboardScreen(
                            onNavigate = { route ->
                                navController.navigate(route)
                            }
                        )
                    }

                    composable("negocios") {
                        NegociosScreen()
                    }

                    composable("locales") {
                        LocalesScreen()
                    }

                    composable("usuarios") {
                        UsuariosScreen()
                    }

                    composable("licencias") {
                        LicenciasScreen()
                    }
                }
            }
        }
    }
}
