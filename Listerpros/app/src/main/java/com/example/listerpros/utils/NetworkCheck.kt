package com.example.listerpros.utils

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build



class NetworkCheck(context: Context) {

    private val connectionManager : ConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager


        fun checkNetwork(): Boolean{
        var result  = false

        if (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                connectionManager.activeNetwork!= null
            } else {
                connectionManager.isDefaultNetworkActive
            })
        {

            result = true
        }

        return result
    }


}