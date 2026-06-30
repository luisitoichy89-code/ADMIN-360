package com.admin360.core.device

import android.content.Context
import android.provider.Settings

object DeviceInfo {

    fun getAndroidId(context: Context): String {
        return Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID
        )
    }
}
