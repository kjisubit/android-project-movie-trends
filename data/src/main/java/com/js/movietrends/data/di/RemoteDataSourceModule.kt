package com.js.movietrends.data.di

import com.js.movietrends.data.api.MovieApi
import com.js.movietrends.data.datasource.RemoteDataSource
import com.js.movietrends.data.datasourceimpl.RemoteDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RemoteDataSourceModule {

    @Provides
    fun provideRemoteDataSource(movieApi: MovieApi):
            RemoteDataSource = RemoteDataSourceImpl(movieApi = movieApi)
}