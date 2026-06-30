package com.admin360.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.admin360.core.session.SessionManager
import com.admin360.ui.screens.*

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
        composable(AppRoutes.LOGIN) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(AppRoutes.DASHBOARD) {
                        popUpTo(AppRoutes.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        // 📊 DASHBOARD SUPER ADMIN
        composable(AppRoutes.DASHBOARD) {
            DashboardScreen(
                session = session,
                onNavigate = { route ->
                    navController.navigate(route)
                }
            )
        }

        // 🏢 NEGOCIOS
        composable(AppRoutes.NEGOCIOS) {
            NegociosScreen(
                onBack = { navController.popBackStack() }
            )
        }

        // 🏬 LOCALES
        composable(AppRoutes.LOCALES) {
            LocalesScreen(
                onBack = { navController.popBackStack() }
            )
        }

        // 👤 USUARIOS
        composable(AppRoutes.USUARIOS) {
            UsuariosScreen(
                onBack = { navController.popBackStack() }
            )
        }

        // 🔑 LICENCIAS
        composable(AppRoutes.LICENCIAS) {
            LicenciasScreen(
                onBack = { navController.popBackStack() }
            )
        }

        // ⚙️ CONFIGURACIÓN
        composable(AppRoutes.CONFIGURACION) {
            ConfiguracionScreen(
                onBack = { navController.popBackStack() }
            )
        }

        // 📄 LOGS
        composable(AppRoutes.LOGS) {
            LogsScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}
