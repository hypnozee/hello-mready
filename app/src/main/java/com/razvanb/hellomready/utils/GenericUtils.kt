package com.razvanb.hellomready.utils

import android.content.Context
import android.net.ConnectivityManager
import android.widget.ImageView
import java.net.InetAddress
import java.net.UnknownHostException
import android.content.pm.PackageManager
import com.razvanb.hellomready.BuildConfig
import com.razvanb.hellomready.data.prefs.PreferenceCore
import com.razvanb.hellomready.data.prefs.SharedPrefConstants
import com.razvanb.hellomready.data.prefs.PreferenceCore.set
import com.razvanb.hellomready.data.prefs.PreferenceCore.get
import com.razvanb.hellomready.di.module.GlideApp

object GenericUtils {

    fun isFirstInstall(context: Context): Boolean {
        return try {
            val firstInstallTime = context.packageManager.getPackageInfo(BuildConfig.APPLICATION_ID, 0).firstInstallTime
            val lastUpdateTime = context.packageManager.getPackageInfo(BuildConfig.APPLICATION_ID, 0).lastUpdateTime
            firstInstallTime == lastUpdateTime
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            true
        }
    }

    fun isInstallFromUpdate(context: Context): Boolean {
        val mDefaultPreferences = PreferenceCore.defaultPrefs(context)
        return try {
            val firstInstallTime = context.packageManager.getPackageInfo(BuildConfig.APPLICATION_ID, 0).firstInstallTime
            val lastUpdateTime = context.packageManager.getPackageInfo(BuildConfig.APPLICATION_ID, 0).lastUpdateTime
            val lastStoredUpdateTime: Long =
                mDefaultPreferences[SharedPrefConstants.PREF_LAST_APP_UPDATE_TIME, 0L] ?: 0L
            mDefaultPreferences[SharedPrefConstants.PREF_LAST_APP_UPDATE_TIME] = lastUpdateTime
            lastUpdateTime > lastStoredUpdateTime && firstInstallTime <= lastUpdateTime
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            false
        }
    }

    fun load(context: Context, url: String, imageView: ImageView) {
        GlideApp
            .with(context)
            .load(url)
            .centerInside()
            .into(imageView)
    }

    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo.isConnected
    }

    fun isInternetAvailable(): Boolean {
        try {
            val address = InetAddress.getByName("www.google.com")
            return !address.equals("")
        } catch (e: UnknownHostException) {
            // Log error
        }
        return false
    }
}
