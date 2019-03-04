package com.razvanb.hellomready.data.db.dao

import android.arch.persistence.room.*
import com.razvanb.hellomready.data.db.table.Repositories

@Dao
interface RepositoriesDao {

    @Query("SELECT * FROM Repositories")
    @Transaction
    fun getAll() : List<Repositories>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrReplaceRepository(repository: Repositories)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrReplaceRepositories(repositories: List<Repositories>)

    @Query("DELETE FROM Repositories WHERE `key` = :id")
    fun deleteRepository(id : String)

    @Query("DELETE FROM Repositories")
    fun deleteAll()
}