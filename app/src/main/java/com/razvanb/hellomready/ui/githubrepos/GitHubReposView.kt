package com.razvanb.hellomready.ui.githubrepos

import com.razvanb.hellomready.data.network.pojo.RepositoriesResponse
import com.razvanb.hellomready.ui.base.BaseView

interface GitHubReposView : BaseView {
    fun onGetReposFinished(isSuccessful: Boolean)
    fun onGetReposStarted(isSuccessful: Boolean)

    fun onLoadRepos(repositoriesResponse: RepositoriesResponse?)

    fun onLoadLayout(layout: Int)
    fun onLayoutChanged(layout: Int)
}