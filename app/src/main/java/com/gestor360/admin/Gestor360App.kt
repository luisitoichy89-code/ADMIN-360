package com.gestor360.admin

import android.app.Application
import android.util.Log
import com.gestor360.admin.presentation.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class Gestor360App : Application() {

    override fun onCreate() {
        super.onCreate()

        initLogger()
        initKoin()
    }

    private fun initLogger() {
        Log.i(TAG, "Gestor360 ADMIN iniciado.")
    }

    private fun initKoin() {
        startKoin {
            androidContext(this@Gestor360App)
            modules(appModule)
        }
    }

    companion object {
        private const val TAG = "Gestor360App"
    }
}
