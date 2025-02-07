package com.js.movietrends.data.repository

import androidx.paging.PagingData
import androidx.paging.map
import com.js.movietrends.data.datasource.RemoteDataSource
import com.js.movietrends.data.mapper.MovieMapper
import com.js.movietrends.domain.model.Movie
import com.js.movietrends.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MovieRepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
) : MovieRepository {
    override fun getNowPlayingMovies(): Flow<PagingData<Movie>> {
        return remoteDataSource.getNowPlayingMovies()
            .map { pagingData ->
                pagingData.map { movieEntity ->
                    MovieMapper.mapEntityToDomain(movieEntity)
                }
            }
    }

    override fun getTopRatedMovies(): Flow<PagingData<Movie>> {
        return remoteDataSource.getTopRatedMovies()
            .map { pagingData ->
                pagingData.map { movieResponse ->
                    MovieMapper.mapDtoToDomain(movieResponse)
                }
            }
    }

    override fun getUpcomingMovies(): Flow<PagingData<Movie>> {
        return remoteDataSource.getUpcomingMovies()
            .map { pagingData ->
                pagingData.map { movieResponse ->
                    MovieMapper.mapDtoToDomain(movieResponse)
                }
            }
    }
}