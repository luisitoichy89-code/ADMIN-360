package com.admin360.core.security

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import com.admin360.ui.screens.SplashGate
import kotlinx.coroutines.launch

@Composable
fun AppLicenseGate(
    supabase: io.github.jan.supabase.SupabaseClient,
    navController: NavHostController,
    content: @Composable () -> Unit
) {

    val scope = remember { kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.Main) }

    LaunchedEffect(Unit) {

        scope.launch {

            val ok = LicenseGuard.validateOrBlock(supabase)

            if (!ok) {
                navController.navigate("blocked") {
                    popUpTo(0)
                }
            }
        }
    }

    when {

        LicenseGuard.isBlocked() -> {

            SplashGate(
                message = "NEGOCIO BLOQUEADO - LICENCIA INACTIVA"
            )

        }

        LicenseGuard.isAllowed() -> {

            content()

        }

        else -> {

            SplashGate(
                message = "Validando licencia..."
            )
        }
    }
}
