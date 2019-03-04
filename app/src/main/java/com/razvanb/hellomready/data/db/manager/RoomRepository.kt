package com.razvanb.hellomready.data.db.manager

import com.razvanb.hellomready.data.db.GitHubRepoDatabase
import com.razvanb.hellomready.data.db.table.Repositories
import javax.inject.Inject


class RoomRepository
@Inject constructor(private val gitHubRepoDatabase: GitHubRepoDatabase) {

    fun insertOrReplaceRepository(repository: Repositories) {
        return gitHubRepoDatabase.repositoriesDao().insertOrReplaceRepository(repository)
    }

    fun deleteRepository(id: String) {
        return gitHubRepoDatabase.repositoriesDao().deleteRepository(id)
    }

    fun retrieveRepository(): List<Repositories> {
        return gitHubRepoDatabase.repositoriesDao().getAll()
    }
}