package com.js.movietrends.domain.repository

import androidx.paging.PagingData
import com.js.movietrends.domain.model.ApiResult
import com.js.movietrends.domain.model.Movie
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    suspend fun getWeeklySpotlight(
        startDate: String,
        endDate: String,
        sortBy: String
    ): ApiResult<Movie>

    fun getNowPlayingMovies(): Flow<PagingData<Movie>>
    fun getUpcomingMovies(): Flow<PagingData<Movie>>
}