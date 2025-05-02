package com.js.movietrends.data.datasource

import com.js.movietrends.data.dto.MovieListResponseDto

interface RemoteDataSource {
    suspend fun getDiscoveredMovies(
        startDate: String,
        endDate: String,
        sortBy: String
    ): MovieListResponseDto

    suspend fun getNowPlayingMovies(page: Int): MovieListResponseDto
    suspend fun getUpcomingMovies(page: Int): MovieListResponseDto
}