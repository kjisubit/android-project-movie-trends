package com.js.movietrends.data.paging.remotemediator

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.js.movietrends.data.database.entity.MovieEntity
import com.js.movietrends.data.database.entity.MovieRemoteKeyEntity
import com.js.movietrends.data.datasource.LocalDataSource
import com.js.movietrends.data.datasource.RemoteDataSource
import com.js.movietrends.data.utils.ErrorMessages

@OptIn(ExperimentalPagingApi::class)
class NowPlayingMovieMediator(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
) : RemoteMediator<Int, MovieEntity>() {

    companion object {
        private const val TAG = "NowPlayingMovieMediator"
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MovieEntity>,
    ): MediatorResult {
        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val remoteKeyEntity = getRemoteKeyEntityOfLastItem(state)
                    remoteKeyEntity?.nextPage
                        ?: return MediatorResult.Success(endOfPaginationReached = remoteKeyEntity != null)
                }
            }

            val response = remoteDataSource.getNowPlayingMovies(page)
            if (response.results.isNullOrEmpty()) {
                return MediatorResult.Success(endOfPaginationReached = true)
            }

            localDataSource.saveNowPlayingMovies(loadType, response)
            MediatorResult.Success(endOfPaginationReached = response.results.isEmpty())
        } catch (e: Exception) {
            Log.e(TAG, e.message ?: ErrorMessages.UNEXPECTED_ERROR, e)
            MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyEntityOfLastItem(state: PagingState<Int, MovieEntity>): MovieRemoteKeyEntity? {
        val lastPageWithData = state.pages.lastOrNull { it.data.isNotEmpty() }
        val lastMovie = lastPageWithData?.data?.lastOrNull()
        val movieId = lastMovie?.id ?: return null
        return localDataSource.getMovieRemoteKeys(movieId)
    }
}