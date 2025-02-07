package com.js.movietrends.data.datasource

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.js.movietrends.data.api.MovieApi
import com.js.movietrends.data.database.MovieDb
import com.js.movietrends.data.database.entity.MovieEntity
import com.js.movietrends.data.model.MovieResponse
import com.js.movietrends.data.paging.pagingsource.TopRatedMoviePagingSource
import com.js.movietrends.data.paging.pagingsource.UpcomingMoviePagingSource
import com.js.movietrends.data.paging.remoteMediator.NowPlayingMovieMediator
import kotlinx.coroutines.flow.Flow

class RemoteDataSourceImpl(private val movieApi: MovieApi, private val movieDb: MovieDb) :
    RemoteDataSource {
    private val movieDao = movieDb.movieDao()

    @OptIn(ExperimentalPagingApi::class)
    override fun getNowPlayingMovies(): Flow<PagingData<MovieEntity>> {
        val pagingSourceFactory = { movieDao.getAllMovies() }
        return Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = NowPlayingMovieMediator(
                movieApi,
                movieDb
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    override fun getTopRatedMovies(): Flow<PagingData<MovieResponse>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { TopRatedMoviePagingSource(movieApi) }
        ).flow
    }

    override fun getUpcomingMovies(): Flow<PagingData<MovieResponse>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { UpcomingMoviePagingSource(movieApi) }
        ).flow
    }
}