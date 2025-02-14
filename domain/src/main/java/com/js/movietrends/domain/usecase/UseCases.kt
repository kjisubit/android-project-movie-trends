package com.js.movietrends.domain.usecase

data class UseCases(
    val getBestRatedMovieUseCase: GetBestRatedMovieUseCase,
    val getNowPlayingMoviesUseCase: GetNowPlayingMoviesUseCase,
    val getUpcomingMoviesUseCase: GetUpcomingMoviesUseCase,
)