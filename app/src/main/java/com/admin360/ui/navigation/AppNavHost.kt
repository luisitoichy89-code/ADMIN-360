package com.admin360.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.admin360.core.session.SessionManager
import com.admin360.ui.screens.*
import com.admin360.ui.screens.admin.AdminPanelScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: String
) {

    val session = SessionManager.session.value

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        // 🔐 LOGIN
        composable("login") {
            LoginScreen(
                onSuccess = {
                    navController.navigate("dashboard") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        // 📊 DASHBOARD
        composable("dashboard") {
            DashboardScreen(
                onNavigate = { route ->
                    navController.navigate(route)
                }
            )
        }

        // 🏢 NEGOCIOS
        composable("negocios") {
            NegociosScreen(
                onBack = { navController.popBackStack() }
            )
        }

        // 🏬 LOCALES
        composable("locales") {
            LocalesScreen(
                onBack = { navController.popBackStack() }
            )
        }

        // 👤 USUARIOS
        composable("usuarios") {
            UsuariosScreen(
                onBack = { navController.popBackStack() }
            )
        }

        // 🔑 LICENCIAS
        composable("licencias") {
            LicenciasScreen(
                onBack = { navController.popBackStack() }
            )
        }

        // ⚙️ ADMIN PANEL
        composable("admin_panel") {
            AdminPanelScreen(
                onNavigate = { route ->
                    navController.navigate(route)
                }
            )
        }

        // 🚫 BLOQUEADO
        composable("blocked") {
            BlockedScreen()
        }
    }
}
