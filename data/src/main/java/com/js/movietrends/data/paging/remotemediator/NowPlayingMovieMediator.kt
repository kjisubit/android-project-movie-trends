package com.js.movietrends.data.paging.remoteMediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.js.movietrends.data.database.entity.MovieEntity
import com.js.movietrends.data.database.entity.MovieRemoteKeyEntity
import com.js.movietrends.data.datasource.LocalDataSource
import com.js.movietrends.data.datasource.RemoteDataSource
import com.js.movietrends.domain.model.ApiResult

@OptIn(ExperimentalPagingApi::class)
class NowPlayingMovieMediator(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : RemoteMediator<Int, MovieEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MovieEntity>
    ): MediatorResult {
        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val remoteKey = getRemoteKeyEntityOfLastItem(state)
                    remoteKey?.nextPage
                        ?: return MediatorResult.Success(endOfPaginationReached = true)
                }
            }

            when (val result = remoteDataSource.getNowPlayingMovies(page)) {
                is ApiResult.Success -> {
                    localDataSource.saveNowPlayingMovies(loadType, result.data)
                    MediatorResult.Success(endOfPaginationReached = result.data.results.isNullOrEmpty())
                }

                is ApiResult.Error -> {
                    MediatorResult.Error(result.exception)
                }

                is ApiResult.Loading -> {
                    MediatorResult.Success(endOfPaginationReached = false)
                }
            }
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyEntityOfLastItem(
        state: PagingState<Int, MovieEntity>,
    ): MovieRemoteKeyEntity? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { movie ->
                localDataSource.getMovieRemoteKeys(movieId = movie.id)
            }
    }
}