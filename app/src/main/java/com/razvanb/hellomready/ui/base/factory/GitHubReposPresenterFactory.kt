package com.razvanb.hellomready.ui.base.factory

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.razvanb.hellomready.data.DataManager
import com.razvanb.hellomready.manager.CoroutinesManager
import com.razvanb.hellomready.ui.githubrepos.GitHubReposPresenter
import javax.inject.Inject

class GitHubReposPresenterFactory
@Inject constructor(private val coroutinesManager: CoroutinesManager, private val dataManager: DataManager) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return GitHubReposPresenter(coroutinesManager, dataManager) as T
    }
}