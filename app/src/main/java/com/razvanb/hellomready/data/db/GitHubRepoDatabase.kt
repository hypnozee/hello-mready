package com.razvanb.hellomready.data.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.razvanb.hellomready.data.db.dao.RepositoriesDao
import com.razvanb.hellomready.data.db.table.Repositories

@Database(entities = [Repositories::class],
        version = 1)
abstract class GitHubRepoDatabase : RoomDatabase() {
    abstract fun repositoriesDao(): RepositoriesDao
}

