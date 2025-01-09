package com.js.movietrends.data.repository

import com.js.movietrends.data.datasource.RemoteDataSource
import com.js.movietrends.domain.repository.MovieRepository

class MovieRepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
) : MovieRepository {
    override fun getNowPlayingMovies() = remoteDataSource.getNowPlayingMovies()
    override fun getPopularMovies() = remoteDataSource.getPopularMovies()
    override fun getTopRatedMovies() = remoteDataSource.getTopRatedMovies()
    override fun getUpcomingMovies() = remoteDataSource.getUpcomingMovies()
}