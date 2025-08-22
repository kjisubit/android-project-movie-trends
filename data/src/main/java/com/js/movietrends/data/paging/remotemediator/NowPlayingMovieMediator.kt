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
import com.js.movietrends.data.util.ErrorMessages

/**
 * [RemoteDataSource], [LocalDataSource]에서 가져온 데이터를 페이지 단위로 로드할 수 있도록 변환
 */
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
                // 새로고침 시 첫 페이지 로드
                LoadType.REFRESH -> 1

                // PREPEND 미지원
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)

                // 마지막 아이템의 RemoteKey에 저장된 nextPage 확인
                // RemoteKey == null -> 최초 APPEND 호출 시점에 RemoteKey가 없는 경우 APPEND 트리거 허용
                // RemoteKey != null && nextPage == null -> 불러올 페이지 없으므로 APPEND 트리거 허용 X
                LoadType.APPEND -> {
                    val remoteKeyEntity = getRemoteKeyEntityOfLastItem(state)
                    remoteKeyEntity?.nextPage
                        ?: return MediatorResult.Success(endOfPaginationReached = remoteKeyEntity != null)
                }
            }

            // nextPage가 가리키는 데이터 존재할 경우 로컬 db에 저장
            val response = remoteDataSource.getNowPlayingMovies(page)
            val endOfPagination = response.results.isNullOrEmpty()
            if (!endOfPagination) localDataSource.saveNowPlayingMovies(loadType, response)
            MediatorResult.Success(endOfPaginationReached = endOfPagination)
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