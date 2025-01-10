package com.js.movietrends.data.di

import com.js.movietrends.data.datasource.RemoteDataSource
import com.js.movietrends.data.repository.MovieRepositoryImpl
import com.js.movietrends.domain.repository.MovieRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)

object RepositoryModule {
    @Provides
    fun provideRepository(
        remoteDataSource: RemoteDataSource
    ): MovieRepository = MovieRepositoryImpl(
        remoteDataSource = remoteDataSource,
    )
}