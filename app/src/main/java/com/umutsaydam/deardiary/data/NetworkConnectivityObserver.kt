package com.umutsaydam.deardiary.data

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.umutsaydam.deardiary.domain.ConnectivityObserver
import com.umutsaydam.deardiary.domain.enums.NetworkStatusEnum
import dagger.hilt.android.qualifiers.ApplicationContext

class NetworkConnectivityObserver(
    @ApplicationContext private val context: Context
) : ConnectivityObserver {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    override fun getCurrentStatus(): NetworkStatusEnum {
        val network = connectivityManager.activeNetwork ?: return NetworkStatusEnum.Unavailable
        val capabilities = connectivityManager.getNetworkCapabilities(network)
        return if (capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true) {
            NetworkStatusEnum.Available
        } else {
            NetworkStatusEnum.Unavailable
        }
    }
}