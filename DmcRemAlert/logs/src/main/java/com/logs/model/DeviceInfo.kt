package com.logs.model

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.os.Build
import android.provider.Settings

/**
 * This class is used for get all details of mobile like brand name, model, sdk etc.
 */
class DeviceInfo private constructor(
    val brand: String,
    val deviceId: String,
    val model: String,
    val id: String,
    val sdk: String,
    val manufacturer: String,
    val user: String,
    val type: String,
    val base: String,
    val incremental: String,
    val board: String,
    val host: String,
    val fingerPrint: String,
    val versionCode: String
){
    /**
     * Builder object for device info.
     */
    object Builder {
        @SuppressLint("HardwareIds")
        fun build(contentResolver: ContentResolver): DeviceInfo {
            return DeviceInfo(
                Build.BRAND,
                try {
                    if (contentResolver == null) {
                        ""
                    } else {
                        Settings.Secure.getString(
                            contentResolver,
                            Settings.Secure.ANDROID_ID
                        )
                    }
                } catch (e: Exception) {
                    ""
                },
                Build.MODEL,
                Build.ID,
                Build.VERSION.SDK_INT.toString(),
                Build.MANUFACTURER,
                Build.USER,
                Build.TYPE,
                Build.VERSION.BASE_OS,
                Build.VERSION.INCREMENTAL,
                Build.BOARD,
                Build.HOST,
                Build.FINGERPRINT,
                Build.HARDWARE
            )
        }
    }
}