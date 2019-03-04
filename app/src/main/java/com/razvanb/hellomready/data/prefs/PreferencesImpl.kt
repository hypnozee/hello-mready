package com.razvanb.hellomready.data.prefs

import android.content.Context
import com.razvanb.hellomready.di.scope.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import com.razvanb.hellomready.data.prefs.PreferenceCore.set
import com.razvanb.hellomready.data.prefs.PreferenceCore.get
import com.razvanb.hellomready.data.prefs.SharedPrefConstants.KEY_LIST_LAYOUT
import com.razvanb.hellomready.ui.callbacks.PreferenceHelper
import com.razvanb.hellomready.utils.Constants

@Singleton
class PreferencesImpl @Inject
    internal constructor(
        @ApplicationContext context: Context
        ) : PreferenceHelper {

    private val mDefaultPreferences = PreferenceCore.defaultPrefs(context)

    override fun getReposListLayout(): Int {
        return mDefaultPreferences[KEY_LIST_LAYOUT] ?: Constants.REPO_LIST_LAYOUT
    }

    override fun setReposListLayout(layout: Int) {
        mDefaultPreferences[KEY_LIST_LAYOUT] = layout
    }
}