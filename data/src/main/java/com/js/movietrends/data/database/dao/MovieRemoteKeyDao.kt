package com.js.movietrends.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.js.movietrends.data.database.entity.MovieRemoteKeyEntity

@Dao
interface MovieRemoteKeyDao {
    @Query("SELECT * FROM movie_remote_key_entity WHERE id = :movieId")
    suspend fun getMovieRemoteKeys(movieId: Int): MovieRemoteKeyEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAllMovieRemoteKeys(movieRemoteKeys: List<MovieRemoteKeyEntity>)

    @Query("DELETE FROM movie_remote_key_entity")
    suspend fun deleteAllMovieRemoteKeys()
}