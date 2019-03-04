package com.razvanb.hellomready.di.component

import com.razvanb.hellomready.di.module.SplashModule
import com.razvanb.hellomready.di.scope.PerScreen
import com.razvanb.hellomready.ui.splash.SplashActivity
import com.razvanb.hellomready.ui.splash.SplashPresenter
import dagger.Subcomponent

@PerScreen
@Subcomponent(modules = [(SplashModule::class)])
interface SplashComponent{
    fun inject(splashPresenter: SplashPresenter)
    fun inject(splashPresenter: SplashActivity)
}