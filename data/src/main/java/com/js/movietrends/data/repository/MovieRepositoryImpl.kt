package com.js.movietrends.data.repository

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.js.movietrends.data.datasource.LocalDataSource
import com.js.movietrends.data.datasource.RemoteDataSource
import com.js.movietrends.data.paging.pagingsource.UpcomingMoviePagingSource
import com.js.movietrends.data.paging.remoteMediator.NowPlayingMovieMediator
import com.js.movietrends.data.utils.ModelMapper
import com.js.movietrends.domain.model.ApiResult
import com.js.movietrends.domain.model.Movie
import com.js.movietrends.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class MovieRepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : MovieRepository {

    companion object {
        private const val TAG = "MovieRepository"
    }

    override fun getWeeklySpotlightedMovie(): Flow<ApiResult<Movie>> = flow {
        emit(ApiResult.Loading)
        when (val result = remoteDataSource.getWeeklySpotlightedMovie()) {
            is ApiResult.Success -> {
                try {
                    val bestRatedMovie = result.data.results!![0]
                    emit(ApiResult.Success(ModelMapper.mapMovieResponseToDomain(bestRatedMovie)))
                } catch (e: Exception) {
                    Log.e(TAG, e.message ?: "", e)
                    emit(ApiResult.Error(e))
                }
            }

            is ApiResult.Error -> emit(result)
            is ApiResult.Loading -> {} // 이미 emit 했으니 무시
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getNowPlayingMovies(): Flow<PagingData<Movie>> {
        val pagingSourceFactory = { localDataSource.getAllMovies() }

        return Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = NowPlayingMovieMediator(remoteDataSource, localDataSource),
            pagingSourceFactory = pagingSourceFactory
        ).flow.map { pagingData ->
            pagingData.map { entity -> ModelMapper.mapMovieEntityToDomain(entity) }
        }
    }

    override fun getUpcomingMovies(): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { UpcomingMoviePagingSource(remoteDataSource) }
        ).flow.map { pagingData ->
            pagingData.map { dto -> ModelMapper.mapMovieResponseToDomain(dto) }
        }
    }

}