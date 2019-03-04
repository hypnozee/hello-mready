package com.razvanb.hellomready.di.component

import com.razvanb.hellomready.di.module.GitHubRepoDetailsModule
import com.razvanb.hellomready.di.scope.PerScreen
import com.razvanb.hellomready.ui.githubrepodetatils.GitHubRepoDetailsActivity
import com.razvanb.hellomready.ui.githubrepodetatils.GitHubRepoDetailsPresenter
import dagger.Subcomponent

@PerScreen
@Subcomponent(modules = [(GitHubRepoDetailsModule::class)])
interface GitHubRepoDetailsComponent{
    fun inject(gitHubRepoDetailsPresenter: GitHubRepoDetailsPresenter)
    fun inject(gitHubRepoDetailsActivity: GitHubRepoDetailsActivity)
}