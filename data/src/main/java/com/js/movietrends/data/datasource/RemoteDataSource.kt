package com.js.movietrends.data.datasource

import androidx.paging.PagingData
import com.js.movietrends.data.database.entity.MovieEntity
import com.js.movietrends.data.model.MovieListResponse
import com.js.movietrends.data.model.MovieResponse
import com.js.movietrends.domain.model.ApiResult
import kotlinx.coroutines.flow.Flow

interface RemoteDataSource {
    fun getBestRatedMovieOfToday(): Flow<ApiResult<MovieListResponse>>
    fun getNowPlayingMovies(): Flow<PagingData<MovieEntity>>
    fun getUpcomingMovies(): Flow<PagingData<MovieResponse>>
}