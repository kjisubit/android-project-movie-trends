package com.js.movietrends.data.datasource

import androidx.paging.LoadType
import androidx.paging.PagingSource
import com.js.movietrends.data.database.entity.NowPlayingMovieEntity
import com.js.movietrends.data.database.entity.MovieRemoteKeyEntity
import com.js.movietrends.data.database.entity.WeeklySpotlightEntity
import com.js.movietrends.data.dto.MovieListResponseDto

interface LocalDataSource {
    // Weekly Spotlight
    suspend fun getWeeklySpotlight(): WeeklySpotlightEntity?
    suspend fun replaceWeeklySpotlight(entity: WeeklySpotlightEntity)

    // Now Playing - 영화
    fun getNowPlayingMovies(): PagingSource<Int, NowPlayingMovieEntity>
    suspend fun addNowPlayingMovies(movies: List<NowPlayingMovieEntity>)
    suspend fun deleteAllNowPlayingMovies()

    // Now Playing - RemoteKey
    suspend fun getMovieRemoteKeys(movieId: Int): MovieRemoteKeyEntity?
    suspend fun addRemoteKeys(keys: List<MovieRemoteKeyEntity>)
    suspend fun deleteAllMovieRemoteKeys()

    // Now Playing - Mediator 저장 (위 함수들을 트랜잭션으로 묶음)
    suspend fun saveNowPlayingMovies(loadType: LoadType, response: MovieListResponseDto)
}
