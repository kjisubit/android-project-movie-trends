package com.js.movietrends.data.datasource

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.js.movietrends.data.BuildConfig
import com.js.movietrends.data.api.MovieApi
import com.js.movietrends.data.database.MovieDb
import com.js.movietrends.data.database.entity.MovieEntity
import com.js.movietrends.data.model.MovieListResponse
import com.js.movietrends.data.model.MovieResponse
import com.js.movietrends.data.paging.pagingsource.UpcomingMoviePagingSource
import com.js.movietrends.data.paging.remoteMediator.NowPlayingMovieMediator
import com.js.movietrends.data.utils.ApiResponseHandler
import com.js.movietrends.domain.core.AppInfoManager
import com.js.movietrends.domain.model.ApiResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RemoteDataSourceImpl(private val movieApi: MovieApi, private val movieDb: MovieDb) :
    RemoteDataSource {

    companion object {
        private const val TAG = "RemoteDataSourceImpl"
    }

    private val movieDao = movieDb.movieDao()

    override fun getBestRatedMovieOfToday(): Flow<ApiResult<MovieListResponse>> {
        return flow {
            emit(ApiResult.Loading)
            try {
                val response = movieApi.getTopRatedMovies(
                    apiKey = BuildConfig.TMDB_API_KEY,
                    language = AppInfoManager.localeCode,
                    page = 1
                )
                val result = ApiResponseHandler.handleApiResponse(response)
                emit(result)
            } catch (e: Exception) {
                Log.e(TAG, e.message ?: "", e)
                emit(ApiResult.Error(e))
            }
        }
    }

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

    override fun getUpcomingMovies(): Flow<PagingData<MovieResponse>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { UpcomingMoviePagingSource(movieApi) }
        ).flow
    }
}