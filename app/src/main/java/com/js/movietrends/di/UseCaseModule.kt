package com.js.movietrends.di

import com.js.movietrends.domain.repository.MovieRepository
import com.js.movietrends.domain.usecase.GetNowPlayingMoviesUseCase
import com.js.movietrends.domain.usecase.GetUpcomingMoviesUseCase
import com.js.movietrends.domain.usecase.GetWeeklySpotlightedMovieUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    fun provideGetWeeklySpotlightedMovieUseCase(repository: MovieRepository) =
        GetWeeklySpotlightedMovieUseCase(repository)

    @Provides
    fun provideGetNowPlayingMoviesUseCase(repository: MovieRepository) =
        GetNowPlayingMoviesUseCase(repository)

    @Provides
    fun provideGetUpcomingMoviesUseCase(repository: MovieRepository) =
        GetUpcomingMoviesUseCase(repository)
}