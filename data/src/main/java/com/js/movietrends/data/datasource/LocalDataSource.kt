package com.js.movietrends.data.datasource

import androidx.paging.LoadType
import androidx.paging.PagingSource
import com.js.movietrends.data.database.entity.NowPlayingMovieEntity
import com.js.movietrends.data.database.entity.MovieRemoteKeyEntity
import com.js.movietrends.data.database.entity.WeeklySpotlightEntity
import com.js.movietrends.data.dto.MovieListResponseDto

interface LocalDataSource {
    suspend fun getWeeklySpotlight(): WeeklySpotlightEntity?
    suspend fun replaceWeeklySpotlight(entity: WeeklySpotlightEntity)

    fun getNowPlayingMovies(): PagingSource<Int, NowPlayingMovieEntity>
    suspend fun getMovieRemoteKeys(movieId: Int): MovieRemoteKeyEntity?
    suspend fun deleteAllNowPlayingMovies()
    suspend fun deleteAllMovieRemoteKeys()
    suspend fun addNowPlayingMovies(movies: List<NowPlayingMovieEntity>)
    suspend fun addRemoteKeys(keys: List<MovieRemoteKeyEntity>)
    suspend fun saveNowPlayingMovies(loadType: LoadType, response: MovieListResponseDto)
}
