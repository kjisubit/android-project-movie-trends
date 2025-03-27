package com.js.movietrends.domain.usecase

data class UseCases(
    val getWeeklySpotlightedMovieUseCase: GetWeeklySpotlightedMovieUseCase,
    val getNowPlayingMoviesUseCase: GetNowPlayingMoviesUseCase,
    val getUpcomingMoviesUseCase: GetUpcomingMoviesUseCase,
)