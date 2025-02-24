package com.js.movietrends.domain.usecase

data class UseCases(
    val getBestRatedMovieUseCase: GetWeeklySpotlightedMovieUseCase,
    val getNowPlayingMoviesUseCase: GetNowPlayingMoviesUseCase,
    val getUpcomingMoviesUseCase: GetUpcomingMoviesUseCase,
)