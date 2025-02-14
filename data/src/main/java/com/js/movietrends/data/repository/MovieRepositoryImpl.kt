package com.js.movietrends.data.repository

import android.util.Log
import androidx.paging.PagingData
import androidx.paging.map
import com.js.movietrends.data.datasource.RemoteDataSource
import com.js.movietrends.data.utils.ModelMapper
import com.js.movietrends.domain.model.ApiResult
import com.js.movietrends.domain.model.Movie
import com.js.movietrends.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MovieRepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
) : MovieRepository {

    companion object {
        private const val TAG = "MovieRepository"
    }

    override fun getBestRatedMovieOfToday(): Flow<ApiResult<Movie>> {
        return remoteDataSource.getBestRatedMovieOfToday().map { apiResult ->
            when (apiResult) {
                is ApiResult.Success -> try {
                    // 영화 리스트 최상위 아이템 하나만 사용
                    val bestRatedMovie = apiResult.data.results!![0]
                    ApiResult.Success(ModelMapper.mapMovieResponseToDomain(bestRatedMovie))
                } catch (e: Exception) {
                    Log.e(TAG, e.message ?: "", e)
                    ApiResult.Error(e)
                }

                is ApiResult.Error -> apiResult
                is ApiResult.Loading -> apiResult
            }
        }
    }

    override fun getNowPlayingMovies(): Flow<PagingData<Movie>> {
        return remoteDataSource.getNowPlayingMovies().map { pagingData ->
            pagingData.map { movieEntity ->
                ModelMapper.mapMovieEntityToDomain(movieEntity)
            }
        }
    }

    override fun getUpcomingMovies(): Flow<PagingData<Movie>> {
        return remoteDataSource.getUpcomingMovies().map { pagingData ->
            pagingData.map { movieResponse ->
                ModelMapper.mapMovieResponseToDomain(movieResponse)
            }
        }
    }
}