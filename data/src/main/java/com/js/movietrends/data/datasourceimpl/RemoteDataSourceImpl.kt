package com.js.movietrends.data.datasourceimpl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.js.movietrends.data.api.MovieApi
import com.js.movietrends.data.datasource.RemoteDataSource
import com.js.movietrends.data.paging.pagingsource.NowPlayingMoviePagingSource
import com.js.movietrends.data.paging.pagingsource.PopularMoviePagingSource
import com.js.movietrends.data.paging.pagingsource.TopRatedMoviePagingSource
import com.js.movietrends.data.paging.pagingsource.UpcomingMoviePagingSource
import com.js.movietrends.domain.model.Movie
import kotlinx.coroutines.flow.Flow

class RemoteDataSourceImpl(private val movieApi: MovieApi) : RemoteDataSource {
    override fun getNowPlayingMovies(): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { NowPlayingMoviePagingSource(movieApi) }
        ).flow
    }

    override fun getPopularMovies(): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { PopularMoviePagingSource(movieApi) }
        ).flow
    }

    override fun getTopRatedMovies(): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { TopRatedMoviePagingSource(movieApi) }
        ).flow
    }

    override fun getUpcomingMovies(): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { UpcomingMoviePagingSource(movieApi) }
        ).flow
    }
}