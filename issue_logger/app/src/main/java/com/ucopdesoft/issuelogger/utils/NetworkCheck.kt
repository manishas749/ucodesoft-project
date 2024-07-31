package com.ucopdesoft.issuelogger.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET
import android.net.NetworkRequest
import androidx.lifecycle.LiveData


class   NetworkCheck(context: Context) : LiveData<Enum<NetworkStatus>>() {

    private lateinit var networkCallback: ConnectivityManager.NetworkCallback
    private val connectivityManager: ConnectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private val validNetworks: MutableSet<Network> = HashSet()

    //This function returns network connected or disconnected.
    fun isOnline(): Enum<NetworkStatus> {
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                return NetworkStatus.CONNECTED
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                return NetworkStatus.CONNECTED
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                return NetworkStatus.CONNECTED
            }
        }
        return NetworkStatus.DISCONNECTED
    }

    //For check network is valid or not.
    fun checkValidNetworks() {
        if (validNetworks.size > 0) {
            postValue(NetworkStatus.CONNECTED)
        } else {
            postValue(NetworkStatus.DISCONNECTED)
        }
    }

    //NetworkRequest to inform ConnectivityManager of what kind of networks it wants to listen to.
    override fun onActive() {
        networkCallback = createNetworkCallback()
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NET_CAPABILITY_INTERNET)
            .build()
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
    }

    //This function used for unregister network
    override fun onInactive() {
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }

    //When the default network changes, it should register a default network callback as follows.
    private fun createNetworkCallback() = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            validNetworks.add(network)
            checkValidNetworks()
        }

        override fun onLost(network: Network) {
            validNetworks.remove(network)
            checkValidNetworks()
        }
    }

}

enum class NetworkStatus {
    CONNECTED,
    DISCONNECTED
}