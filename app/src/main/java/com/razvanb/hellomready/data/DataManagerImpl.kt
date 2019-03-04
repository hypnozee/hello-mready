package com.razvanb.hellomready.data

import com.razvanb.hellomready.data.network.api.NetworkApi
import com.razvanb.hellomready.data.network.pojo.Readme
import com.razvanb.hellomready.ui.callbacks.PreferenceHelper
import com.razvanb.hellomready.data.network.pojo.RepositoriesResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataManagerImpl @Inject
    internal constructor(
    private val mPrefHelper: PreferenceHelper,
    private val mNetworkApi: NetworkApi
        ) : DataManager{

    override fun getReposListLayout(): Int {
        return mPrefHelper.getReposListLayout()
    }

    override fun setReposListLayout(layout: Int) {
        mPrefHelper.setReposListLayout(layout)
    }

    override fun getRepositories(
        qualifier: String?,
        sort: String?,
        order: String?
    ): Deferred<Response<RepositoriesResponse>> {
        return mNetworkApi.getRepositories(
            qualifier,
            sort,
            order)
    }

    override fun getReadme(owner: String,
                           repo: String
    ): Deferred<Response<Readme>> {
        return mNetworkApi.getReadme(
            owner,
            repo)
    }
}