package com.razvanb.hellomready.di.component

import com.razvanb.hellomready.di.module.GitHubReposModule
import com.razvanb.hellomready.di.scope.PerScreen
import com.razvanb.hellomready.ui.githubrepos.GitHubReposActivity
import com.razvanb.hellomready.ui.githubrepos.GitHubReposPresenter
import dagger.Subcomponent

@PerScreen
@Subcomponent(modules = [(GitHubReposModule::class)])
interface GitHubReposComponent{
    fun inject(gitHubReposPresenter: GitHubReposPresenter)
    fun inject(gitHubReposActivity: GitHubReposActivity)
}