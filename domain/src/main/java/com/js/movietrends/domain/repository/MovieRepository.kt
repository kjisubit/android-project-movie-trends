package com.js.movietrends.domain.repository

import androidx.paging.PagingData
import com.js.movietrends.domain.model.ApiResult
import com.js.movietrends.domain.model.Movie
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun getDiscoveredMovies(
        startDate: String,
        endDate: String,
        sortBy: String
    ): Flow<ApiResult<Movie>>

    fun getNowPlayingMovies(): Flow<PagingData<Movie>>
    fun getUpcomingMovies(): Flow<PagingData<Movie>>
}