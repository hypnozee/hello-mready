package com.razvanb.hellomready.ui.base

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.razvanb.hellomready.utils.isNetworkAvailable

/**
 * Base Activity which provides required methods and presenter
 * instantiation and calls.
 */
abstract class BaseActivity : BaseView, AppCompatActivity() {
    private var savedInstanceState: Bundle? = null

    private val networkIntentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)

    private val networkReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (isNetworkAvailable) {

            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.savedInstanceState = savedInstanceState

        injectDependencies()
        setUpPresenter()
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(networkReceiver, networkIntentFilter)
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(networkReceiver)
    }

    abstract fun injectDependencies()

    abstract fun setUpPresenter()

    override fun getContext(): Context {
        return this
    }
}