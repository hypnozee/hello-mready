package com.razvanb.hellomready.ui.githubrepos

import com.razvanb.hellomready.data.DataManager
import com.razvanb.hellomready.data.db.manager.RoomRepository
import com.razvanb.hellomready.data.network.pojo.RepositoriesResponse
import com.razvanb.hellomready.manager.ApplicationState
import com.razvanb.hellomready.manager.CoroutinesManager
import com.razvanb.hellomready.ui.base.BasePresenterImpl
import com.razvanb.hellomready.di.module.GitHubReposModule
import com.razvanb.hellomready.utils.Constants
import timber.log.Timber
import javax.inject.Inject

class GitHubReposPresenter(coroutinesManager: CoroutinesManager, dataManager: DataManager) :
    BasePresenterImpl<GitHubReposView>(coroutinesManager, dataManager) {


    @Inject
    internal lateinit var roomRepository: RoomRepository

    override fun onInjectDependencies() {
        ApplicationState
            .get()
            .getAppComponent()
            .inject(GitHubReposModule())
            .inject(this)
    }

    fun getReposFromApi() {
        launchOnUI {
            val request = getRepositories(Constants.QUALIFIER, Constants.SORT, Constants.ORDER)
            val response = request.await()
            if (response.isSuccessful) {
                val reposResponse: RepositoriesResponse? = response.body()
                onSuccessRepos(reposResponse)
                Timber.d("Request successful")
            }
        }
    }

    private suspend fun onSuccessRepos(repositoriesResponse: RepositoriesResponse?) {
        view().onLoadRepos(repositoriesResponse)
    }

    fun changeLayout(layout: Int) {
        setReposListLayout(layout)
        launchOnUI {
            onLayoutChanged(layout)
        }
    }

    private suspend fun onLayoutChanged(layout: Int) {
        view().onLayoutChanged(layout)
    }
}