package com.js.movietrends.data.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.js.movietrends.data.database.entity.NowPlayingMovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NowPlayingMovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNowPlayingMovies(movies: List<NowPlayingMovieEntity>)

    // room-paging 의존성에 의해 PagingSource가 자동 생성됨
    // LIMIT/OFFSET 페이징은 순서가 고정되어야 하므로 저장 순서(pk)로 정렬
    @Query("SELECT * FROM now_playing_movie_entity ORDER BY pk")
    fun getNowPlayingMovies(): PagingSource<Int, NowPlayingMovieEntity>

    @Query("SELECT * FROM now_playing_movie_entity WHERE id = :movieId")
    fun getMovie(movieId: Int): Flow<NowPlayingMovieEntity>

    @Query("DELETE FROM now_playing_movie_entity")
    suspend fun deleteAllNowPlayingMovies()
}