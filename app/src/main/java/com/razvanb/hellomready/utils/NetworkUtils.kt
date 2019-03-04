package com.razvanb.hellomready.utils

import android.content.Context
import android.net.ConnectivityManager

internal val Context.isNetworkAvailable: Boolean
    get() = with(getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager) {
        if (this.activeNetworkInfo == null) false
        else this.activeNetworkInfo.isConnected
    }