package com.tuapp.core.navigation

import androidx.navigation.NavHostController

class AppNavigator(
    private val navController: NavHostController
) {

    fun dashboard() {
        navController.navigate(AppRoutes.DASHBOARD)
    }

    fun negocios() {
        navController.navigate(AppRoutes.NEGOCIOS)
    }

    fun locales() {
        navController.navigate(AppRoutes.LOCALES)
    }

    fun usuarios() {
        navController.navigate(AppRoutes.USUARIOS)
    }

    fun licencias() {
        navController.navigate(AppRoutes.LICENCIAS)
    }

    fun back() {
        navController.popBackStack()
    }

    fun logout() {

        navController.navigate(AppRoutes.LOGIN) {

            popUpTo(0)

            launchSingleTop = true

        }

    }

}
