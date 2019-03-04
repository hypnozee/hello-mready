package com.razvanb.hellomready.data.network.api

import com.razvanb.hellomready.data.network.pojo.Readme
import com.razvanb.hellomready.data.network.pojo.RepositoriesResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface NetworkApi {

    @Headers("accept: application/json")
    @GET("/search/repositories")
    fun getRepositories(
            @Query("q") qualifier: String?,
            @Query("sort") sort: String?,
            @Query("order") order: String?
    ): Deferred<Response<RepositoriesResponse>>

    @Headers("accept: application/json")
    @GET("/repos/{owner}/{repo}/readme")
    fun getReadme(
        @Path("owner") owner: String,
        @Path("repo") repo: String
    ): Deferred<Response<Readme>>
}