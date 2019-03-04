package com.razvanb.hellomready.di.module

import android.app.Application
import android.content.Context
import com.razvanb.hellomready.data.DataManager
import com.razvanb.hellomready.data.DataManagerImpl
import com.razvanb.hellomready.data.prefs.PreferencesImpl
import com.razvanb.hellomready.ui.callbacks.PreferenceHelper
import com.razvanb.hellomready.di.scope.ApplicationContext
import com.razvanb.hellomready.di.scope.PreferenceInfo
import com.razvanb.hellomready.manager.CoroutinesManager
import com.razvanb.hellomready.manager.DefaultCoroutinesManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Provides the Application
 * @param application the Application object used for retriving application class
 */
@Module
class ApplicationModule constructor(private val application: Application) {

    @Provides
    internal fun provideApplication(): Application = application

    @Provides
    @ApplicationContext
    internal fun provideApplicationContext(): Context = application

    @Provides
    internal fun provideCoroutinesManager(): CoroutinesManager {
        return DefaultCoroutinesManager()
    }

    @Provides
    @Singleton
    internal fun provideDataManger(dataManager: DataManagerImpl): DataManager {
        return dataManager
    }

    @Provides
    @Singleton
    internal fun providePreferencesHelper(appPreferencesHelper: PreferencesImpl): PreferenceHelper {
        return appPreferencesHelper
    }

    @Provides
    @PreferenceInfo
    internal fun providePreferenceName(): String {
        return "settings"
    }
}