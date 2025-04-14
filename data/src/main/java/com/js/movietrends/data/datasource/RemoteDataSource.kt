package com.js.movietrends.data.datasource

import com.js.movietrends.data.dto.MovieListResponseDto

interface RemoteDataSource {
    suspend fun getWeeklySpotlightedMovie(): MovieListResponseDto
    suspend fun getNowPlayingMovies(page: Int): MovieListResponseDto
    suspend fun getUpcomingMovies(page: Int): MovieListResponseDto
}