package com.rentreadychecklist.utils

import android.content.Context
import android.net.ConnectivityManager


/**
 * This class for check network is active or not.
 */
class NetworkCheck(context: Context) {

    private val connectionManager: ConnectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    fun checkNetwork(): Boolean {
        var result = false

        if (
            connectionManager.activeNetwork != null
        ) {
            result = true
        }
        return result
    }


}