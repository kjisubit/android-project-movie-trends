package com.js.movietrends.di

import com.js.movietrends.data.api.MovieApi
import com.js.movietrends.data.database.MovieDb
import com.js.movietrends.data.datasource.RemoteDataSource
import com.js.movietrends.data.datasource.RemoteDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteDataSourceModule {

    @Provides
    @Singleton
    fun provideRemoteDataSource(movieApi: MovieApi, movieDb: MovieDb):
            RemoteDataSource = RemoteDataSourceImpl(movieApi = movieApi, movieDb)
}