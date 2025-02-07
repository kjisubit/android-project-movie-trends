package com.js.movietrends.domain.usecase

data class UseCases(
    val getNowPlayingMoviesUseCase: GetNowPlayingMoviesUseCase,
    val getTopRatedMoviesUseCase: GetTopRatedMoviesUseCase,
    val getUpcomingMoviesUseCase: GetUpcomingMoviesUseCase,
)