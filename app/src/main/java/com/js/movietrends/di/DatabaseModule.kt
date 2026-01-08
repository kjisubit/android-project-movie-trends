package com.js.movietrends.di

import android.app.Application
import androidx.room.Room
import com.js.movietrends.data.database.MovieDb
import com.js.movietrends.data.database.dao.MovieDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application): MovieDb =
        Room.databaseBuilder(app, MovieDb::class.java, "movie_database")
            .fallbackToDestructiveMigration(true).build()

    @Provides
    @Singleton
    fun provideMovieDao(movieDB: MovieDb): MovieDao = movieDB.movieDao()
}