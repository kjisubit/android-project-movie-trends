package com.js.movietrends.data.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.js.movietrends.data.database.entity.MovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMovies(movies: List<MovieEntity>)

    @Query("SELECT * FROM movie_entity")
    fun getAllMovies(): PagingSource<Int, MovieEntity>

    @Query("SELECT * FROM movie_entity WHERE id = :movieId")
    fun getMovie(movieId: Int): Flow<MovieEntity>

    @Query("DELETE FROM movie_entity")
    suspend fun deleteAllMovies()
}