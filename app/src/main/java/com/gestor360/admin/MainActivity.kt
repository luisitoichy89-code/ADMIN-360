package com.gestor360.admin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.gestor360.admin.presentation.ui.dashboard.DashboardScreen
import com.gestor360.admin.presentation.ui.login.LoginScreen
import com.gestor360.admin.presentation.ui.theme.Gestor360Theme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Gestor360AdminApp()
        }
    }
}

@Composable
private fun Gestor360AdminApp() {

    val navController = rememberNavController()

    Gestor360Theme {

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {

            NavHost(
                navController = navController,
                startDestination = Routes.LOGIN
            ) {

                composable(Routes.LOGIN) {
                    LoginScreen(navController = navController)
                }

                composable(Routes.DASHBOARD) {
                    DashboardScreen(navController = navController)
                }
            }
        }
    }
}

private object Routes {
    const val LOGIN = "login"
    const val DASHBOARD = "dashboard"
}
