package com.js.movietrends.data.datasource

import androidx.paging.LoadType
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.js.movietrends.data.database.entity.MovieEntity
import com.js.movietrends.data.database.entity.MovieRemoteKeyEntity
import com.js.movietrends.data.dto.MovieListResponseDto

interface LocalDataSource {
    fun getAllMovies(): PagingSource<Int, MovieEntity>
    suspend fun getMovieRemoteKeys(movieId: Int): MovieRemoteKeyEntity?
    suspend fun deleteAllMovies()
    suspend fun deleteAllMovieRemoteKeys()
    suspend fun addMovies(movies: List<MovieEntity>)
    suspend fun addRemoteKeys(keys: List<MovieRemoteKeyEntity>)
    suspend fun getLastRemoteKey(state: PagingState<Int, MovieEntity>): MovieRemoteKeyEntity?
    suspend fun saveNowPlayingMovies(loadType: LoadType, response: MovieListResponseDto)
}