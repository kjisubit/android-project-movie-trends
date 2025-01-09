package com.js.movietrends.domain.usecase

data class UseCases(
    val getPopularMoviesUseCase: GetPopularMoviesUseCase,
    val getNowPlayingMoviesUseCase: GetNowPlayingMoviesUseCase,
    val getTopRatedMoviesUseCase: GetTopRatedMoviesUseCase,
    val getUpcomingMoviesUseCase: GetUpcomingMoviesUseCase,
)