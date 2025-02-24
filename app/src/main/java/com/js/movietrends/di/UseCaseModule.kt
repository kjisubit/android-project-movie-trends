package com.js.movietrends.di

import com.js.movietrends.domain.repository.MovieRepository
import com.js.movietrends.domain.usecase.GetNowPlayingMoviesUseCase
import com.js.movietrends.domain.usecase.GetUpcomingMoviesUseCase
import com.js.movietrends.domain.usecase.GetWeeklySpotlightedMovieUseCase
import com.js.movietrends.domain.usecase.UseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {

    @Provides
    fun provideUseCases(movieRepository: MovieRepository) = UseCases(
        getBestRatedMovieUseCase = GetWeeklySpotlightedMovieUseCase(movieRepository),
        getNowPlayingMoviesUseCase = GetNowPlayingMoviesUseCase(movieRepository),
        getUpcomingMoviesUseCase = GetUpcomingMoviesUseCase(movieRepository)
    )
}