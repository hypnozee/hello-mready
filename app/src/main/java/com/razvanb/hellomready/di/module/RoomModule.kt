package com.razvanb.hellomready.di.module

import android.app.Application
import android.arch.persistence.room.Room
import com.razvanb.hellomready.data.db.GitHubRepoDatabase
import com.razvanb.hellomready.data.db.dao.RepositoriesDao
import com.razvanb.hellomready.data.db.manager.RoomRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RoomModule(mApplication: Application) {

    private val gitHubRepoDatabase: GitHubRepoDatabase =
        Room.databaseBuilder(mApplication, GitHubRepoDatabase::class.java, "github_repos_database.db")
            .build()

    @Singleton
    @Provides
    internal fun providesRoomDatabase(): GitHubRepoDatabase {
        return gitHubRepoDatabase
    }

    @Singleton
    @Provides
    internal fun providesEffectDao(gitHubRepoDatabase: GitHubRepoDatabase): RepositoriesDao {
        return gitHubRepoDatabase.repositoriesDao()
    }

    @Singleton
    @Provides
    internal fun roomRepository(gitHubRepoDatabase: GitHubRepoDatabase): RoomRepository {
        return RoomRepository(gitHubRepoDatabase)
    }

}