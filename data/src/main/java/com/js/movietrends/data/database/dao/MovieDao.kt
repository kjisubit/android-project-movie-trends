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
    suspend fun addNowPlayingMovies(movies: List<MovieEntity>)

    // room-paging 의존성에 의해 PagingSource가 자동 생성됨
    // LIMIT/OFFSET 페이징은 순서가 고정되어야 하므로 저장 순서(pk)로 정렬
    @Query("SELECT * FROM movie_entity ORDER BY pk")
    fun getNowPlayingMovies(): PagingSource<Int, MovieEntity>

    @Query("SELECT * FROM movie_entity WHERE id = :movieId")
    fun getMovie(movieId: Int): Flow<MovieEntity>

    @Query("DELETE FROM movie_entity")
    suspend fun deleteAllNowPlayingMovies()
}