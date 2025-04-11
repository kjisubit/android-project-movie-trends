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
    private val localDataSource: LocalDataSource,
) : RemoteMediator<Int, MovieEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MovieEntity>,
    ): MediatorResult {
        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)

                // 최초 APPEND 호출 시점에는 페이징 데이터가 존재하지 않는다.
                // 따라서 페이징 데이터의 마지막 아이템의 Remote Key 정보를 가져올 수 없다.
                // RemoteDataSource 로부터 받아온 데이터는 데이터베이스에 저장되어 있으므로,
                // 데이터베이스의 마지막 아이템의 Remote Key로부터 nextPage 값을 가져오도록 한다.
                // 공식 문서에도 "Query remoteKeyDao for the next RemoteKey"라고 되어 있다.
                LoadType.APPEND -> {
                    val remoteKeyEntity = if (isPagingDataEmpty(state)) {
                        localDataSource.getLastRemoteKeyFromDb()
                    } else {
                        getRemoteKeyEntityOfLastItem(state)
                    }

                    remoteKeyEntity?.nextPage
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

    /**
     * 페이징 데이터 마지막 아이템의 remoteKeyEntity 가져오기
     */
    private suspend fun getRemoteKeyEntityOfLastItem(state: PagingState<Int, MovieEntity>): MovieRemoteKeyEntity? {
        val lastPageWithData = state.pages.lastOrNull { it.data.isNotEmpty() }
        val lastMovie = lastPageWithData?.data?.lastOrNull()
        val movieId = lastMovie?.id ?: return null
        return localDataSource.getMovieRemoteKeys(movieId)
    }

    /**
     * 페이징 데이터 존재 여부 확인
     */
    private fun isPagingDataEmpty(state: PagingState<Int, MovieEntity>): Boolean {
        val size = state.pages.lastOrNull()?.data?.size
        return size == 0
    }
}