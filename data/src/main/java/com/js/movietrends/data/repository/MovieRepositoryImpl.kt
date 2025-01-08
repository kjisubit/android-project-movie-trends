//package com.js.movietrends.data.repository
//
//import com.js.movietrends.data.datasource.RemoteDataSource
//import com.js.movietrends.domain.model.Movie
//import com.js.movietrends.domain.repository.MovieRepository
//import kotlinx.coroutines.flow.Flow
//
//class MovieRepositoryImpl(
//    private val remoteDataSource: RemoteDataSource,
//) : MovieRepository {
//    override fun getPopularMovies(): Flow<Movie> = remoteDataSource
//}