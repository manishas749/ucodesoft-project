package com.example.listerpros.utils

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import com.example.listerpros.constants.Constants.Companion.LONG
import com.google.android.gms.location.LocationServices

class GetLogitudeLatitude(applicationContext: Context)
{

    var latitude: Double = 1.0
    var longitude: Double =0.0
    val context=applicationContext

    fun getLongitudeLatitude():Pair<Double,Double> {

         var fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
         val requestCode = 101


        if (ActivityCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                 Activity(),
                arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION),
                requestCode
            )
        }
        val task = fusedLocationProviderClient.lastLocation

        task.addOnSuccessListener { location ->
            if (location != null) {
                val currentLocation = location
                 LONG = currentLocation.latitude
                Log.d("lat", LONG.toString())
                longitude = currentLocation.longitude

            }
        }

         return Pair(latitude,longitude)
     }
}