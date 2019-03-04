package com.razvanb.hellomready.di.component

import android.app.Application
import android.content.Context
import com.razvanb.hellomready.data.db.manager.RoomRepository
import com.razvanb.hellomready.di.module.*
import com.razvanb.hellomready.di.scope.ApplicationContext
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    ApplicationModule::class,
    RoomModule::class,
    NetworkModule::class])

interface ApplicationComponent {

    @ApplicationContext
    fun getContext(): Context

    fun inject(application: Application)
    fun inject(module: SplashModule): SplashComponent
    fun inject(module: GitHubReposModule): GitHubReposComponent
    fun inject(module: GitHubRepoDetailsModule): GitHubRepoDetailsComponent
    fun roomRepository(): RoomRepository
}