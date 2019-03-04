package com.razvanb.hellomready.manager

import com.razvanb.hellomready.BuildConfig
import com.razvanb.hellomready.di.component.ApplicationComponent
import android.support.multidex.MultiDex
import android.support.multidex.MultiDexApplication
import com.razvanb.hellomready.data.prefs.SharedPrefConstants
import com.razvanb.hellomready.di.module.ApplicationModule
import com.razvanb.hellomready.di.module.RoomModule
import com.razvanb.hellomready.utils.GenericUtils
import com.razvanb.hellomready.data.prefs.PreferenceCore
import com.razvanb.hellomready.utils.TypefaceUtil
import timber.log.Timber
import com.razvanb.hellomready.data.prefs.PreferenceCore.set
import com.razvanb.hellomready.di.component.DaggerApplicationComponent
import com.razvanb.hellomready.di.module.NetworkModule


class ApplicationState : MultiDexApplication(){

    companion object {
        private lateinit var app: ApplicationState
        lateinit var component: ApplicationComponent
        fun get(): ApplicationState = app
    }

    override fun onCreate() {
        super.onCreate()

        TypefaceUtil.overrideFont(applicationContext, "SERIF","fonts/futura_regular.ttf") // font from assets: "assets/fonts/FuturaPT-Bold.otf

        // Initialize dagger
        app = this
        MultiDex.install(this)
        component = DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(this))
                .roomModule(RoomModule(this))
                .networkModule(NetworkModule())
                .build()
        component.inject(this)

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        val mDefaultPreferences = PreferenceCore.defaultPrefs(applicationContext)
        mDefaultPreferences[SharedPrefConstants.PREF_IS_FRESH_INSTALL_OR_UPDATE] = GenericUtils.isFirstInstall(context = applicationContext) || GenericUtils.isInstallFromUpdate(context = applicationContext)
    }

    fun app(): ApplicationState {
        return this
    }

    fun getAppComponent(): ApplicationComponent = component

}