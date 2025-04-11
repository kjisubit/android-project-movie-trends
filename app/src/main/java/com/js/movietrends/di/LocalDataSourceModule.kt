package com.js.movietrends.di


import com.js.movietrends.data.database.MovieDb
import com.js.movietrends.data.datasource.LocalDataSource
import com.js.movietrends.data.datasource.LocalDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalDataSourceModule {

    @Provides
    @Singleton
    fun provideLocalDataSource(
        movieDb: MovieDb
    ): LocalDataSource = LocalDataSourceImpl(
        movieDb = movieDb
    )
}