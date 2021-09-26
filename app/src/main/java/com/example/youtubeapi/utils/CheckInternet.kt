package com.example.youtubeapi.utils

import android.content.Context
import android.annotation.SuppressLint
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build

class CheckInternet(private val context: Context) {

    val available: Boolean
        @SuppressLint("MissingPermission")
        get() {
            if (context == null) return false
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            return when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
                    val nc = cm.getNetworkCapabilities(cm.activeNetwork)
                    if (nc != null) {
                        nc.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                                nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                                nc.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
                    } else false
                }
                else -> {
                    val info = cm.allNetworkInfo
                    for (i in info.indices) info[i].state == NetworkInfo.State.CONNECTED
                    false
                }
            }
            return false
        }

}