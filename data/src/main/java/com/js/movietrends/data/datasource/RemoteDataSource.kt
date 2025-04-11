package com.js.movietrends.data.datasource

import com.js.movietrends.data.dto.MovieListResponseDto
import com.js.movietrends.domain.model.ApiResult

interface RemoteDataSource {
    suspend fun getWeeklySpotlightedMovie(): ApiResult<MovieListResponseDto>

    suspend fun getNowPlayingMovies(page: Int): ApiResult<MovieListResponseDto>

    suspend fun getUpcomingMovies(page: Int): ApiResult<MovieListResponseDto>
}