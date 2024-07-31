package com.rentreadychecklist

import android.app.Application
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import androidx.appcompat.app.AppCompatDelegate


class RentReadyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        val builder = VmPolicy.Builder()

        //Set this mode for PDF generate
        StrictMode.setVmPolicy(builder.build())
    }
}