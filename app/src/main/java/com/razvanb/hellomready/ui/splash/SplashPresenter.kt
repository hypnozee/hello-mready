package com.razvanb.hellomready.ui.splash

import com.razvanb.hellomready.data.DataManager
import com.razvanb.hellomready.manager.CoroutinesManager
import com.razvanb.hellomready.ui.base.BasePresenterImpl

class SplashPresenter(coroutinesManager: CoroutinesManager, dataManager: DataManager) :
    BasePresenterImpl<SplashView>(coroutinesManager,dataManager)
