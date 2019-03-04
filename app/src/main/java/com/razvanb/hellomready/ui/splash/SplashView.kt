package com.razvanb.hellomready.ui.splash

import com.razvanb.hellomready.ui.base.BaseView

interface SplashView : BaseView {
    fun onGetReposFinished(isSuccessful: Boolean)
    fun onGetReposStarted(isSuccessful: Boolean)
}